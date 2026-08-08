package com.pokemon.mypokecardex.mapper;

import com.pokemon.mypokecardex.dto.external.TcgDexSetResponseDto;
import com.pokemon.mypokecardex.entity.catalogue.PokemonSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TcgDexSetMapper {

    public PokemonSet toEntity(TcgDexSetResponseDto source) {

        if (source == null) {
            return null;
        }

        PokemonSet pokemonSet = PokemonSet.builder()
                .apiId(source.getId())
                .name(source.getName())
                .logoUrl(source.getLogo())
                .build();

        if (source.getReleaseDate() != null) {
            pokemonSet.setReleaseDate(
                    LocalDate.parse(source.getReleaseDate())
            );
        }

        if (source.getSerie() != null) {
            pokemonSet.setSeries(
                    source.getSerie().getName()
            );
        }

        return pokemonSet;
    }
}
