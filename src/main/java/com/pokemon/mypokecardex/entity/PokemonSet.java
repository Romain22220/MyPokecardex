package com.pokemon.mypokecardex.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pokemon_set")
public class PokemonSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String apiId;


    @Column(nullable = false)
    private String name;


    private String series;


    private LocalDate releaseDate;


    private String logoUrl;


    @OneToMany(
            mappedBy = "pokemonSet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<PokemonCard> cards = new ArrayList<>();
}
