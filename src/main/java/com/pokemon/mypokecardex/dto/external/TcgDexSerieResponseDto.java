package com.pokemon.mypokecardex.dto.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TcgDexSerieResponseDto {

    private String id;

    private String name;

    private String logo;

    private List<TcgDexSetBriefResponseDto> sets;
}