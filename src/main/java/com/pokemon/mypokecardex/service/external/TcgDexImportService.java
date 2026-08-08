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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TcgDexImportService {

    private static final String POKEMON_TCG_POCKET_SERIES_ID = "tcgp";
    private final TcgDexClient tcgDexClient;
    private final TcgDexSetMapper tcgDexSetMapper;
    private final PokemonSetRepository pokemonSetRepository;

    @Transactional
    public int importPhysicalSets() {
        log.info("=== Début de l'import des sets Pokémon TCG ===");
        List<TcgDexSerieResponseDto> series = tcgDexClient.getSeries();

        log.info("{} séries récupérées depuis TCGdex", series.size());

        int imported = 0;
        int processedSets = 0;
        int totalSets = 0;

        for (TcgDexSerieResponseDto serie : series) {

            if (!isPhysicalTcgSerie(serie)) {
                log.debug(
                        "Série ignorée : {} ({})",
                        serie != null ? serie.getName() : "inconnue",
                        serie != null ? serie.getId() : "null"
                );
                continue;
            }

            TcgDexSerieResponseDto detailedSerie = tcgDexClient.getSerie(serie.getId());

            if (detailedSerie == null ||
                    detailedSerie.getSets() == null) {
                log.warn(
                        "Impossible de récupérer les sets de la série {}",
                        serie.getId()
                );
                continue;
            }

            totalSets += detailedSerie.getSets().size();
        }

        log.info("{} sets physiques à traiter", totalSets);

        for (TcgDexSerieResponseDto serie : series) {

            if (!isPhysicalTcgSerie(serie)) {
                continue;
            }

            log.info(
                    "Traitement de la série : {} ({})",
                    serie.getName(),
                    serie.getId()
            );

            TcgDexSerieResponseDto detailedSerie = tcgDexClient.getSerie(serie.getId());

            if (detailedSerie == null ||
                    detailedSerie.getSets() == null) {
                continue;
            }

            for (TcgDexSetBriefResponseDto set : detailedSerie.getSets()) {
                processedSets++;

                log.info(
                        "[{}/{}] Import du set : {} ({})",
                        processedSets,
                        totalSets,
                        set.getName(),
                        set.getId()
                );

                try {

                    TcgDexSetResponseDto detailedSet = tcgDexClient.getSet(set.getId());

                    if (detailedSet == null) {
                        log.warn("Set {} introuvable sur TCGdex", set.getId());
                        continue;
                    }

                    int result = importOrUpdateSet(detailedSet);
                    imported += result;

                    log.info(
                            "Set {} terminé ({})",
                            set.getId(),
                            result == 1
                                    ? "nouveau"
                                    : "déjà présent / mis à jour"
                    );

                } catch (Exception exception) {

                    log.error(
                            "Erreur lors de l'import du set {} ({})",
                            set.getId(),
                            set.getName(),
                            exception
                    );
                }
            }
        }

        log.info(
                "=== Import des sets terminé : {} nouveaux sets sur {} ===",
                imported,
                processedSets
        );

        return imported;
    }

    private boolean isPhysicalTcgSerie(TcgDexSerieResponseDto serie) {

        if (serie == null || serie.getId() == null) {
            return false;
        }

        return !POKEMON_TCG_POCKET_SERIES_ID.equalsIgnoreCase(serie.getId());
    }

    private int importOrUpdateSet(TcgDexSetResponseDto externalSet) {

        PokemonSet pokemonSet =
                pokemonSetRepository
                        .findByApiId(externalSet.getId())
                        .orElse(null);

        if (pokemonSet == null) {
            pokemonSet = tcgDexSetMapper.toEntity(externalSet);
            pokemonSetRepository.save(pokemonSet);
            return 1;
        }

        updateSet(pokemonSet, externalSet);
        pokemonSetRepository.save(pokemonSet);

        return 0;
    }

    private void updateSet(PokemonSet pokemonSet, TcgDexSetResponseDto externalSet) {

        pokemonSet.setName(externalSet.getName());
        pokemonSet.setLogoUrl(externalSet.getLogo());

        if (externalSet.getReleaseDate() != null) {
            pokemonSet.setReleaseDate(LocalDate.parse(externalSet.getReleaseDate()));
        }

        if (externalSet.getSerie() != null) {
            pokemonSet.setSeries(externalSet.getSerie().getName());
        }
    }
}