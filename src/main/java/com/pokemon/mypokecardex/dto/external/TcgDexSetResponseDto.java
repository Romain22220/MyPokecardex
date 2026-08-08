package com.pokemon.mypokecardex.dto.external;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TcgDexSetResponseDto {
    private String id;

    private String name;

    private String logo;

    private String symbol;

    private String releaseDate;

    private CardCount cardCount;

    private Serie serie;

    private List<TcgDexCardBriefResponseDto> cards;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CardCount {

        private Integer total;

        private Integer official;

        private Integer reverse;

        private Integer holo;

        private Integer firstEd;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Serie {

        private String id;

        private String name;
    }
}
