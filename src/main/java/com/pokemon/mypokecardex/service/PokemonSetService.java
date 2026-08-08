package com.pokemon.mypokecardex.service;

import com.pokemon.mypokecardex.dto.PokemonSetDto;
import com.pokemon.mypokecardex.entity.catalogue.PokemonSet;
import com.pokemon.mypokecardex.mapper.PokemonSetMapper;
import com.pokemon.mypokecardex.repository.catalogue.PokemonSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PokemonSetService {
    private final PokemonSetRepository pokemonSetRepository;
    private final PokemonSetMapper pokemonSetMapper;

    public List<PokemonSetDto> findAll() {
        return pokemonSetRepository.findAll()
                .stream()
                .map(pokemonSetMapper::toDto)
                .toList();
    }

    public PokemonSetDto findById(Long id) {
        PokemonSet pokemonSet = pokemonSetRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Set Pokémon introuvable : " + id)
                );

        return pokemonSetMapper.toDto(pokemonSet);
    }
}
