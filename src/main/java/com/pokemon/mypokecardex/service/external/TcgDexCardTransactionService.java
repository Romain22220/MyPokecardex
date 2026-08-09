package com.pokemon.mypokecardex.service.external;

import com.pokemon.mypokecardex.dto.external.TcgDexCardResponseDto;
import com.pokemon.mypokecardex.dto.external.TcgDexCardmarketPricingDto;
import com.pokemon.mypokecardex.dto.external.TcgDexVariantDetailedDto;
import com.pokemon.mypokecardex.dto.external.TcgDexVariantsDto;
import com.pokemon.mypokecardex.entity.catalogue.*;
import com.pokemon.mypokecardex.mapper.TcgDexCardMapper;
import com.pokemon.mypokecardex.mapper.TcgDexCardPrintMapper;
import com.pokemon.mypokecardex.repository.catalogue.CardPrintRepository;
import com.pokemon.mypokecardex.repository.catalogue.PokemonCardRepository;
import com.pokemon.mypokecardex.repository.collection.RarityRepository;
import com.pokemon.mypokecardex.repository.collection.VariantTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TcgDexCardTransactionService {

    private final PokemonCardRepository pokemonCardRepository;
    private final CardPrintRepository cardPrintRepository;
    private final RarityRepository rarityRepository;
    private final VariantTypeRepository variantTypeRepository;

    private final TcgDexCardMapper tcgDexCardMapper;
    private final TcgDexCardPrintMapper tcgDexCardPrintMapper;

    @Transactional
    public void saveCard(
            TcgDexCardResponseDto card,
            PokemonSet pokemonSet
    ) {

        PokemonCard pokemonCard =
                pokemonCardRepository
                        .findByApiId(card.getId())
                        .orElse(null);

        if (pokemonCard == null) {

            pokemonCard =
                    tcgDexCardMapper.toEntity(card);

            pokemonCard.setPokemonSet(pokemonSet);

            pokemonCard =
                    pokemonCardRepository.save(pokemonCard);
        }

        importPrints(card, pokemonCard);
    }

    private int importPrints(
            TcgDexCardResponseDto card,
            PokemonCard pokemonCard
    ) {

        Rarity rarity =
                getOrCreateRarity(
                        card.getRarity()
                );

        List<TcgDexVariantDetailedDto> variants =
                card.getVariantsDetailed();

        if (variants != null && !variants.isEmpty()) {

            int created = 0;

            for (TcgDexVariantDetailedDto variant : variants) {

                if (createPrint(
                        card,
                        pokemonCard,
                        rarity,
                        variant
                )) {
                    created++;
                }
            }

            return created;
        }

        return importFallbackVariants(
                card,
                pokemonCard,
                rarity
        );
    }

    private boolean createPrint(
            TcgDexCardResponseDto card,
            PokemonCard pokemonCard,
            Rarity rarity,
            TcgDexVariantDetailedDto variant
    ) {

        String variantName =
                normalizeVariantType(
                        variant.getType()
                );

        VariantType variantType =
                getOrCreateVariantType(
                        variantName
                );

        Long cardmarketProductId =
                extractCardmarketProductId(
                        variant
                );

        boolean exists =
                cardPrintRepository
                        .findByPokemonCardIdAndCollectorNumberAndVariantTypeId(
                                pokemonCard.getId(),
                                card.getLocalId(),
                                variantType.getId()
                        )
                        .isPresent();

        if (exists) {
            return false;
        }

        CardPrint cardPrint =
                tcgDexCardPrintMapper.toEntity(
                        card.getLocalId(),
                        card.getImage(),
                        cardmarketProductId,
                        variant.getVariantId(),
                        variant.getSize(),
                        pokemonCard,
                        rarity,
                        variantType
                );

        cardPrintRepository.save(cardPrint);

        log.info(
                "CardPrint créé : {} / {} / {} / CM={}",
                card.getId(),
                card.getLocalId(),
                variantName,
                cardmarketProductId
        );

        return true;
    }

    private int importFallbackVariants(
            TcgDexCardResponseDto card,
            PokemonCard pokemonCard,
            Rarity rarity
    ) {

        TcgDexVariantsDto variants =
                card.getVariants();

        if (variants == null) {
            return createSimplePrint(
                    card,
                    pokemonCard,
                    rarity,
                    "NORMAL"
            ) ? 1 : 0;
        }

        int created = 0;

        if (Boolean.TRUE.equals(variants.getNormal())) {
            if (createSimplePrint(
                    card,
                    pokemonCard,
                    rarity,
                    "NORMAL"
            )) {
                created++;
            }
        }

        if (Boolean.TRUE.equals(variants.getHolo())) {
            if (createSimplePrint(
                    card,
                    pokemonCard,
                    rarity,
                    "HOLO"
            )) {
                created++;
            }
        }

        if (Boolean.TRUE.equals(variants.getReverse())) {
            if (createSimplePrint(
                    card,
                    pokemonCard,
                    rarity,
                    "REVERSE_HOLO"
            )) {
                created++;
            }
        }

        if (Boolean.TRUE.equals(variants.getFirstEdition())) {
            if (createSimplePrint(
                    card,
                    pokemonCard,
                    rarity,
                    "FIRST_EDITION"
            )) {
                created++;
            }
        }

        if (Boolean.TRUE.equals(variants.getWPromo())) {
            if (createSimplePrint(
                    card,
                    pokemonCard,
                    rarity,
                    "PROMO"
            )) {
                created++;
            }
        }

        if (created == 0) {
            if (createSimplePrint(
                    card,
                    pokemonCard,
                    rarity,
                    "NORMAL"
            )) {
                created++;
            }
        }

        return created;
    }

    private boolean createSimplePrint(
            TcgDexCardResponseDto card,
            PokemonCard pokemonCard,
            Rarity rarity,
            String variantName
    ) {

        VariantType variantType =
                getOrCreateVariantType(
                        variantName
                );

        boolean exists =
                cardPrintRepository
                        .findByPokemonCardIdAndCollectorNumberAndVariantTypeId(
                                pokemonCard.getId(),
                                card.getLocalId(),
                                variantType.getId()
                        )
                        .isPresent();

        if (exists) {
            return false;
        }

        CardPrint cardPrint =
                tcgDexCardPrintMapper.toEntity(
                        card.getLocalId(),
                        card.getImage(),
                        null,
                        null,
                        null,
                        pokemonCard,
                        rarity,
                        variantType
                );

        cardPrintRepository.save(cardPrint);

        return true;
    }

    private Long extractCardmarketProductId(
            TcgDexVariantDetailedDto variant
    ) {

        if (variant == null ||
                variant.getPricing() == null ||
                variant.getPricing().getCardmarket() == null) {
            return null;
        }

        TcgDexCardmarketPricingDto cardmarket =
                variant.getPricing().getCardmarket();

        return cardmarket.getIdProduct();
    }

    private Rarity getOrCreateRarity(
            String rarityName
    ) {

        if (rarityName == null ||
                rarityName.isBlank()) {
            return null;
        }

        return rarityRepository
                .findByName(rarityName)
                .orElseGet(() -> {

                    Rarity rarity = new Rarity();

                    rarity.setName(rarityName);

                    return rarityRepository.save(rarity);
                });
    }

    private VariantType getOrCreateVariantType(
            String variantName
    ) {

        return variantTypeRepository
                .findByName(variantName)
                .orElseGet(() -> {

                    VariantType variantType =
                            new VariantType();

                    variantType.setName(
                            variantName
                    );

                    return variantTypeRepository.save(
                            variantType
                    );
                });
    }

    private String normalizeVariantType(
            String tcgDexVariant
    ) {

        if (tcgDexVariant == null ||
                tcgDexVariant.isBlank()) {
            return "NORMAL";
        }

        return switch (
                tcgDexVariant
                        .trim()
                        .toLowerCase()
                ) {

            case "normal" -> "NORMAL";

            case "holo" -> "HOLO";

            case "reverse",
                 "reverse holo" -> "REVERSE_HOLO";

            case "1st edition",
                 "first edition" -> "FIRST_EDITION";

            case "w promo",
                 "promo" -> "PROMO";

            default ->
                    tcgDexVariant
                            .trim()
                            .toUpperCase()
                            .replace(" ", "_");
        };
    }
}