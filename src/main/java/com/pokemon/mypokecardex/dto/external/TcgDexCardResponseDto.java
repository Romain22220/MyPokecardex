package com.pokemon.mypokecardex.dto.external;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TcgDexCardResponseDto {
    private String id;

    private String localId;

    private String name;

    private String image;

    private String category;

    private String rarity;

    private String illustrator;

    private String description;

    private TcgDexSetReferenceDto set;

    private TcgDexVariantsDto variants;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TcgDexSetReferenceDto {

        private String id;

        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TcgDexVariantsDto {

        private Boolean firstEdition;

        private Boolean holo;

        private Boolean normal;

        private Boolean reverse;

        private Boolean wPromo;
    }
}
