package com.pokemon.mypokecardex.controller;

import com.pokemon.mypokecardex.dto.PokemonSetDto;
import com.pokemon.mypokecardex.service.PokemonSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sets")
@RequiredArgsConstructor
public class PokemonSetController {
    private final PokemonSetService pokemonSetService;

    @GetMapping
    public List<PokemonSetDto> findAll() {
        return pokemonSetService.findAll();
    }

    @GetMapping("/{id}")
    public PokemonSetDto findById(@PathVariable Long id) {
        return pokemonSetService.findById(id);
    }
}
