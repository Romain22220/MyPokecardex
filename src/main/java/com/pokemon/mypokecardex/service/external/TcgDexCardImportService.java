package com.pokemon.mypokecardex.service.external;

import com.pokemon.mypokecardex.client.TcgDexClient;
import com.pokemon.mypokecardex.dto.external.TcgDexCardResponseDto;
import com.pokemon.mypokecardex.dto.external.TcgDexSetResponseDto;
import com.pokemon.mypokecardex.entity.catalogue.PokemonCard;
import com.pokemon.mypokecardex.entity.catalogue.PokemonSet;
import com.pokemon.mypokecardex.mapper.TcgDexCardMapper;
import com.pokemon.mypokecardex.repository.catalogue.PokemonCardRepository;
import com.pokemon.mypokecardex.repository.catalogue.PokemonSetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TcgDexCardImportService {

    private final TcgDexClient tcgDexClient;

    private final PokemonSetRepository pokemonSetRepository;

    private final PokemonCardRepository pokemonCardRepository;

    private final TcgDexCardMapper tcgDexCardMapper;


    @Transactional
    public int importCardsFromSet(String setId) {
        log.info("=== Début import des cartes du set {} ===", setId);

        PokemonSet pokemonSet =
                pokemonSetRepository
                        .findByApiId(setId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Set inexistant dans la base : " + setId
                                )
                        );

        TcgDexSetResponseDto set = tcgDexClient.getSet(setId);

        if (set == null || set.getCards() == null) {
            log.warn("Aucune carte trouvée pour le set {}", setId);

            return 0;
        }

        int totalCards = set.getCards().size();
        int imported = 0;
        int processed = 0;

        log.info("{} cartes à traiter pour le set {}", totalCards, setId);

        for (TcgDexCardResponseDto card : set.getCards()) {
            processed++;

            if (card == null || card.getId() == null) {
                log.warn("[{}/{}] Carte invalide ignorée", processed, totalCards);

                continue;
            }

            boolean exists = pokemonCardRepository.findByApiId(card.getId()).isPresent();

            if (exists) {
                log.debug("[{}/{}] Carte déjà présente : {}", processed, totalCards, card.getId());

                continue;
            }

            PokemonCard pokemonCard = tcgDexCardMapper.toEntity(card);

            pokemonCard.setPokemonSet(pokemonSet);
            pokemonCardRepository.save(pokemonCard);

            imported++;

            log.info(
                    "[{}/{}] Carte importée : {} - {}",
                    processed,
                    totalCards,
                    card.getId(),
                    card.getName()
            );
        }

        log.info("=== Set {} terminé : {}/{} nouvelles cartes ===", setId, imported, totalCards);

        return imported;
    }


    @Transactional
    public int importCardsFromAllSets() {
        log.info("=== Début import des cartes de tous les sets ===");

        List<PokemonSet> sets = pokemonSetRepository.findAll();

        int totalSets = sets.size();
        int processedSets = 0;
        int imported = 0;

        log.info("{} sets présents en base à traiter", totalSets);

        for (PokemonSet set : sets) {
            processedSets++;

            if (set.getApiId() == null) {
                log.warn("[{}/{}] Set sans apiId ignoré", processedSets, totalSets);

                continue;
            }

            log.info(
                    "---------- [{}/{}] {} ({}) ----------",
                    processedSets,
                    totalSets,
                    set.getName(),
                    set.getApiId()
            );

            try {

                int result = importCardsFromSet(set.getApiId());

                imported += result;

            } catch (Exception exception) {

                log.error(
                        "Erreur lors de l'import des cartes du set {} ({})",
                        set.getApiId(),
                        set.getName(),
                        exception
                );
            }
        }

        log.info("=== Import global terminé : {} nouvelles cartes ===", imported);

        return imported;
    }
}