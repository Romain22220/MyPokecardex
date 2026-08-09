package com.pokemon.mypokecardex.mapper;

import com.pokemon.mypokecardex.entity.catalogue.CardPrint;
import com.pokemon.mypokecardex.entity.catalogue.PokemonCard;
import com.pokemon.mypokecardex.entity.catalogue.Rarity;
import com.pokemon.mypokecardex.entity.catalogue.VariantType;
import org.springframework.stereotype.Component;

@Component
public class TcgDexCardPrintMapper {

    public CardPrint toEntity(
            String collectorNumber,
            String imageUrl,
            Long cardmarketProductId,
            String tcgDexVariantId,
            String variantSize,
            PokemonCard pokemonCard,
            Rarity rarity,
            VariantType variantType
    ) {

        CardPrint cardPrint = new CardPrint();

        cardPrint.setCollectorNumber(collectorNumber);
        cardPrint.setImageUrl(imageUrl);
        cardPrint.setCardmarketProductId(cardmarketProductId);
        cardPrint.setTcgDexVariantId(tcgDexVariantId);
        cardPrint.setVariantSize(variantSize);
        cardPrint.setPokemonCard(pokemonCard);
        cardPrint.setRarity(rarity);
        cardPrint.setVariantType(variantType);

        return cardPrint;
    }
}