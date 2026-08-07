package com.pokemon.mypokecardex.repository.catalogue;

import com.pokemon.mypokecardex.entity.catalogue.PokemonCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonCardRepository extends JpaRepository<PokemonCard, Long> {
    Optional<PokemonCard> findByApiId(String apiId);
}
