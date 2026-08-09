package com.pokemon.mypokecardex.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TcgDexCardmarketPricingDto {
    private String updated;

    private String unit;

    private Long idProduct;

    private Double avg;

    private Double low;

    private Double trend;

    private Double avg1;

    private Double avg7;

    private Double avg30;

    @JsonProperty("avg-holo")
    private Double avgHolo;

    @JsonProperty("low-holo")
    private Double lowHolo;

    @JsonProperty("trend-holo")
    private Double trendHolo;

    @JsonProperty("avg1-holo")
    private Double avg1Holo;

    @JsonProperty("avg7-holo")
    private Double avg7Holo;

    @JsonProperty("avg30-holo")
    private Double avg30Holo;
}
