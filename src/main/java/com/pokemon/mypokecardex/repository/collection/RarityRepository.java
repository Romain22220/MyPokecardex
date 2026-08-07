package com.pokemon.mypokecardex.repository.collection;

import com.pokemon.mypokecardex.entity.catalogue.Rarity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RarityRepository extends JpaRepository<Rarity, Long> {
    Optional<Rarity> findByName(String name);
}
