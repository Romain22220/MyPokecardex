package com.pokemon.mypokecardex.dto.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TcgDexVariantDetailedDto {
    private String type;

    private String size;

    private String variantId;

    private TcgDexPricingDto pricing;

}
