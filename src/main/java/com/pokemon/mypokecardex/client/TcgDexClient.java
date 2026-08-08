package com.pokemon.mypokecardex.client;

import com.pokemon.mypokecardex.dto.external.TcgDexSerieResponseDto;
import com.pokemon.mypokecardex.dto.external.TcgDexSetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TcgDexClient {
    private final RestClient tcgDexRestClient;

    @Value("${tcgdex.api.language}")
    private String language;

    public List<TcgDexSerieResponseDto> getSeries() {

        TcgDexSerieResponseDto[] response = tcgDexRestClient.get()
                .uri("/{language}/series", language)
                .retrieve()
                .body(TcgDexSerieResponseDto[].class);

        if (response == null) {
            return List.of();
        }

        return Arrays.asList(response);
    }

    public TcgDexSerieResponseDto getSerie(String serieId) {

        return tcgDexRestClient.get()
                .uri("/{language}/series/{serieId}", language, serieId)
                .retrieve()
                .body(TcgDexSerieResponseDto.class);
    }

    public TcgDexSetResponseDto getSet(String setId) {

        return tcgDexRestClient.get()
                .uri("/{language}/sets/{setId}", language, setId)
                .retrieve()
                .body(TcgDexSetResponseDto.class);
    }
}
