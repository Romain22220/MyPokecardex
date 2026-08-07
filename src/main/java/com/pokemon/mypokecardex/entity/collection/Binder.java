package com.pokemon.mypokecardex.entity.collection;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "binder")
public class Binder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String name;


    private String description;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private AppUser user;


    @OneToMany(
            mappedBy = "binder",
            cascade = CascadeType.ALL
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BinderLocation> locations = new ArrayList<>();
}
