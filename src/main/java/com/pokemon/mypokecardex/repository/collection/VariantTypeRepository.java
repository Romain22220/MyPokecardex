package com.pokemon.mypokecardex.repository.collection;

import com.pokemon.mypokecardex.entity.catalogue.VariantType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VariantTypeRepository extends JpaRepository<VariantType, Long> {
    Optional<VariantType> findByName(String name);
}
