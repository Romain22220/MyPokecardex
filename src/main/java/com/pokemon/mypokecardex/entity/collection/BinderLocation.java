package com.pokemon.mypokecardex.entity.collection;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "binder_location")
public class BinderLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Integer pageNumber;


    private Integer slotNumber;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "card_copy_id",
            nullable = false,
            unique = true
    )
    private CardCopy cardCopy;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "binder_id",
            nullable = false
    )
    private Binder binder;
}
