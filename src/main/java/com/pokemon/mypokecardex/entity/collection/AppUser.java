package com.pokemon.mypokecardex.entity.collection;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String password;


    @Column(nullable = false, unique = true)
    private String username;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CardCollection> collections = new ArrayList<>();


    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Binder> binders = new ArrayList<>();
}
