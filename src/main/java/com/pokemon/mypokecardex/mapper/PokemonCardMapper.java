package com.pokemon.mypokecardex.mapper;

import com.pokemon.mypokecardex.dto.PokemonCardDto;
import com.pokemon.mypokecardex.entity.catalogue.PokemonCard;
import org.springframework.stereotype.Component;

@Component
public class PokemonCardMapper {
    public PokemonCardDto toDto(PokemonCard entity) {
        if (entity == null) {
            return null;
        }

        return PokemonCardDto.builder()
                .id(entity.getId())
                .apiId(entity.getApiId())
                .name(entity.getName())
                .setId(entity.getPokemonSet().getId())
                .build();
    }
}
