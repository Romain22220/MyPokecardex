package com.pokemon.mypokecardex.mapper;

import com.pokemon.mypokecardex.dto.PokemonSetDto;
import com.pokemon.mypokecardex.entity.catalogue.PokemonSet;
import org.springframework.stereotype.Component;

@Component
public class PokemonSetMapper {
    public PokemonSetDto toDto(PokemonSet entity) {
        if (entity == null) {
            return null;
        }

        return PokemonSetDto.builder()
                .id(entity.getId())
                .apiId(entity.getApiId())
                .name(entity.getName())
                .series(entity.getSeries())
                .releaseDate(entity.getReleaseDate())
                .logoUrl(entity.getLogoUrl())
                .build();
    }
}
