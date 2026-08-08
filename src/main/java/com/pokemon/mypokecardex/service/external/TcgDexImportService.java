package com.pokemon.mypokecardex.service.external;

import com.pokemon.mypokecardex.client.TcgDexClient;
import com.pokemon.mypokecardex.dto.external.TcgDexSerieResponseDto;
import com.pokemon.mypokecardex.dto.external.TcgDexSetBriefResponseDto;
import com.pokemon.mypokecardex.dto.external.TcgDexSetResponseDto;
import com.pokemon.mypokecardex.entity.catalogue.PokemonSet;
import com.pokemon.mypokecardex.mapper.TcgDexSetMapper;
import com.pokemon.mypokecardex.repository.catalogue.PokemonSetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TcgDexImportService {

    private static final String POKEMON_TCG_POCKET_SERIES_ID = "tcgp";

    private final TcgDexClient tcgDexClient;

    private final TcgDexSetMapper tcgDexSetMapper;

    private final PokemonSetRepository pokemonSetRepository;


    @Transactional
    public int importPhysicalSets() {

        List<TcgDexSerieResponseDto> series =
                tcgDexClient.getSeries();

        int imported = 0;

        for (TcgDexSerieResponseDto serie : series) {

            if (!isPhysicalTcgSerie(serie)) {
                continue;
            }

            TcgDexSerieResponseDto detailedSerie =
                    tcgDexClient.getSerie(serie.getId());

            if (detailedSerie == null ||
                    detailedSerie.getSets() == null) {
                continue;
            }

            for (TcgDexSetBriefResponseDto set :
                    detailedSerie.getSets()) {

                TcgDexSetResponseDto detailedSet =
                        tcgDexClient.getSet(set.getId());

                if (detailedSet == null) {
                    continue;
                }

                imported += importOrUpdateSet(detailedSet);
            }
        }

        return imported;
    }


    private boolean isPhysicalTcgSerie(
            TcgDexSerieResponseDto serie
    ) {

        if (serie == null || serie.getId() == null) {
            return false;
        }

        return !POKEMON_TCG_POCKET_SERIES_ID.equalsIgnoreCase(
                serie.getId()
        );
    }


    private int importOrUpdateSet(
            TcgDexSetResponseDto externalSet
    ) {

        PokemonSet pokemonSet =
                pokemonSetRepository
                        .findByApiId(externalSet.getId())
                        .orElse(null);

        if (pokemonSet == null) {

            pokemonSet =
                    tcgDexSetMapper.toEntity(externalSet);

            pokemonSetRepository.save(pokemonSet);

            return 1;
        }

        updateSet(pokemonSet, externalSet);

        pokemonSetRepository.save(pokemonSet);

        return 0;
    }


    private void updateSet(
            PokemonSet pokemonSet,
            TcgDexSetResponseDto externalSet
    ) {

        pokemonSet.setName(
                externalSet.getName()
        );

        pokemonSet.setLogoUrl(
                externalSet.getLogo()
        );

        if (externalSet.getReleaseDate() != null) {

            pokemonSet.setReleaseDate(
                    LocalDate.parse(
                            externalSet.getReleaseDate()
                    )
            );
        }

        if (externalSet.getSerie() != null) {

            pokemonSet.setSeries(
                    externalSet.getSerie().getName()
            );
        }
    }
}
