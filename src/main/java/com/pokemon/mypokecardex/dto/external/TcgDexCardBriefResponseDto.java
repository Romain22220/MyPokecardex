package com.pokemon.mypokecardex.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TcgDexCardBriefResponseDto {
    private String id;
    private String localId;
    private String name;
    private String image;
}
