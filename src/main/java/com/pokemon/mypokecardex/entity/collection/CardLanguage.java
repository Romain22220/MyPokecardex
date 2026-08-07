package com.pokemon.mypokecardex.entity.collection;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "card_language")
@Entity
public class CardLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String code;


    @Column(nullable = false)
    private String name;


    @OneToMany(mappedBy = "language")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CardCopy> cards = new ArrayList<>();
}
