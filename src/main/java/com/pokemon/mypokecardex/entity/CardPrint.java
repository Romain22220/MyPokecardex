package com.pokemon.mypokecardex.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "card_print")
@NoArgsConstructor
@AllArgsConstructor
public class CardPrint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String collectorNumber;


    private String imageUrl;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "card_id",
            nullable = false
    )
    private PokemonCard pokemonCard;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rarity_id")
    private Rarity rarity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_type_id")
    private VariantType variantType;
}
