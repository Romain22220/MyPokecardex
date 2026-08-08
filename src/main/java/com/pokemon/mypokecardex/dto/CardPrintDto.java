package com.pokemon.mypokecardex.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardPrintDto {
    private Long id;

    private String collectorNumber;
    private String imageUrl;
    private Long cardId;
    private Long rarityId;
    private String rarityName;
    private Long variantTypeId;
    private String variantTypeName;
}
