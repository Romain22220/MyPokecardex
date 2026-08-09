package com.pokemon.mypokecardex.dto.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TcgDexLegalDto {
    private Boolean standard;

    private Boolean expanded;
}
