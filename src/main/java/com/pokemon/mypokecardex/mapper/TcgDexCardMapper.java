package com.pokemon.mypokecardex.mapper;

import com.pokemon.mypokecardex.dto.external.TcgDexCardResponseDto;
import com.pokemon.mypokecardex.entity.catalogue.PokemonCard;
import org.springframework.stereotype.Component;

@Component
public class TcgDexCardMapper {

    public PokemonCard toEntity(
            TcgDexCardResponseDto source
    ) {

        if (source == null) {
            return null;
        }

        return PokemonCard.builder()
                .apiId(source.getId())
                .name(source.getName())
                .build();
    }
}
