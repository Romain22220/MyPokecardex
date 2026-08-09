package com.pokemon.mypokecardex.dto.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TcgDexPricingDto {
    private TcgDexCardmarketPricingDto cardmarket;
}
