package com.pokemon.mypokecardex.entity.collection;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "card_collection")
@Builder
public class CardCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDateTime createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private AppUser user;


    @OneToMany(
            mappedBy = "collection",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CardCopy> cards = new ArrayList<>();
}
