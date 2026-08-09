package com.pokemon.mypokecardex.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TcgDexCardResponseDto {

    private String category;

    private String id;

    private String illustrator;

    private String image;

    private String localId;

    private String name;

    private String rarity;

    private TcgDexSetReferenceDto set;

    private TcgDexVariantsDto variants;

    @JsonProperty("variants_detailed")
    private List<TcgDexVariantDetailedDto> variantsDetailed;

    private String effect;

    private String trainerType;

    private String regulationMark;

    private TcgDexLegalDto legal;

    private String updated;

    private TcgDexPricingDto pricing;
}