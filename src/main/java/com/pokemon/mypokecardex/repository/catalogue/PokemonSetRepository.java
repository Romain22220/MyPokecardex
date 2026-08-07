package com.pokemon.mypokecardex.repository.catalogue;

import com.pokemon.mypokecardex.entity.catalogue.PokemonSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonSetRepository extends JpaRepository<PokemonSet, Long> {
    Optional<PokemonSet> findByApiId(String apiId);
}
