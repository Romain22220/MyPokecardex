package com.pokemon.mypokecardex.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PokemonCardDto {
    private Long id;
    private String apiId;
    private String name;
    private Long setId;
}
