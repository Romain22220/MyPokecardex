package com.pokemon.mypokecardex.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PokemonSetDto {
    private Long id;
    private String apiId;
    private String name;
    private String series;
    private LocalDate releaseDate;
    private String logoUrl;
}
