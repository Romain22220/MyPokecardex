package com.pokemon.mypokecardex.entity.collection;

import com.pokemon.mypokecardex.entity.catalogue.CardPrint;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "card_copy")
public class CardCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String conditionState;


    private String grading;


    private BigDecimal purchasePrice;


    private LocalDate purchaseDate;


    @Column(columnDefinition = "TEXT")
    private String notes;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "collection_id",
            nullable = false
    )
    private CardCollection collection;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "card_print_id",
            nullable = false
    )
    private CardPrint cardPrint;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "language_id",
            nullable = false
    )
    private CardLanguage language;
}
