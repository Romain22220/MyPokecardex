package com.pokemon.mypokecardex.repository.catalogue;

import com.pokemon.mypokecardex.entity.catalogue.CardPrint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardPrintRepository extends JpaRepository<CardPrint, Long> {

    Optional<CardPrint> findByPokemonCardIdAndCollectorNumberAndVariantTypeId(
            Long pokemonCardId,
            String collectorNumber,
            Long variantTypeId
    );

    Optional<CardPrint> findByCardmarketProductId(Long cardmarketProductId);
}
