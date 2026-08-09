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
    private CardCountDto cardCount;
    private TcgDexSerieReferenceDto serie;
    private List<TcgDexCardBriefResponseDto> cards;

    @Data
    @NoArgsConstructor
    public static class CardCountDto {
        private Integer official;
        private Integer total;
    }
}
