package com.pokemon.mypokecardex.dto.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TcgDexVariantsDto {

    private Boolean firstEdition;

    private Boolean holo;

    private Boolean normal;

    private Boolean reverse;

    private Boolean wPromo;
}