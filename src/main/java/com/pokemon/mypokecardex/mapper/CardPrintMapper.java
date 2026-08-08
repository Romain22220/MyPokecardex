package com.pokemon.mypokecardex.mapper;

import com.pokemon.mypokecardex.dto.CardPrintDto;
import com.pokemon.mypokecardex.entity.catalogue.CardPrint;
import org.springframework.stereotype.Component;

@Component
public class CardPrintMapper {
    public CardPrintDto toDto(CardPrint entity) {
        if (entity == null) {
            return null;
        }

        return CardPrintDto.builder()
                .id(entity.getId())
                .collectorNumber(entity.getCollectorNumber())
                .imageUrl(entity.getImageUrl())
                .cardId(entity.getPokemonCard().getId())
                .rarityId(
                        entity.getRarity() != null
                                ? entity.getRarity().getId()
                                : null
                )
                .rarityName(
                        entity.getRarity() != null
                                ? entity.getRarity().getName()
                                : null
                )
                .variantTypeId(
                        entity.getVariantType() != null
                                ? entity.getVariantType().getId()
                                : null
                )
                .variantTypeName(
                        entity.getVariantType() != null
                                ? entity.getVariantType().getName()
                                : null
                )
                .build();
    }
}
