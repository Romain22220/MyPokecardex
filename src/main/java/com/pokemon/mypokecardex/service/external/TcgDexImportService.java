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


@Slf4j
@Service
@RequiredArgsConstructor
public class TcgDexImportService {

    private final TcgDexClient tcgDexClient;

    private final PokemonSetRepository pokemonSetRepository;

    private final TcgDexSetMapper tcgDexSetMapper;

    @Transactional
    public int importPhysicalSets() {

        log.info("========================================");
        log.info("Début import des sets Pokémon");
        log.info("========================================");

        var series =
                tcgDexClient.getSeries();

        if (series == null || series.isEmpty()) {

            log.warn("Aucune série trouvée");

            return 0;
        }

        int seriesProcessed = 0;
        int setsImported = 0;
        int setsUpdated = 0;

        for (TcgDexSerieResponseDto serie : series) {

            if (serie == null ||
                    serie.getId() == null) {
                continue;
            }

            /*
             * tcgp = Pokémon TCG Pocket.
             *
             * On ne veut pas importer les cartes du jeu
             * mobile dans notre collection physique.
             */
            if ("tcgp".equalsIgnoreCase(
                    serie.getId()
            )) {

                log.info(
                        "Série ignorée : {} - {}",
                        serie.getId(),
                        serie.getName()
                );

                continue;
            }

            seriesProcessed++;

            log.info(
                    "Traitement série : {} - {}",
                    serie.getId(),
                    serie.getName()
            );

            TcgDexSerieResponseDto serieDetails =
                    tcgDexClient.getSerie(
                            serie.getId()
                    );

            if (serieDetails == null ||
                    serieDetails.getSets() == null) {

                log.warn(
                        "Aucun set pour la série {}",
                        serie.getId()
                );

                continue;
            }

            for (TcgDexSetBriefResponseDto setBrief :
                    serieDetails.getSets()) {

                if (setBrief == null ||
                        setBrief.getId() == null) {
                    continue;
                }

                try {

                    TcgDexSetResponseDto set =
                            tcgDexClient.getSet(
                                    setBrief.getId()
                            );

                    if (set == null) {

                        log.warn(
                                "Set introuvable : {}",
                                setBrief.getId()
                        );

                        continue;
                    }

                    boolean created =
                            importOrUpdateSet(
                                    set,
                                    serie
                            );

                    if (created) {
                        setsImported++;
                    } else {
                        setsUpdated++;
                    }

                } catch (Exception e) {

                    log.error(
                            "Erreur lors de l'import du set {}",
                            setBrief.getId(),
                            e
                    );
                }
            }
        }

        log.info("========================================");
        log.info("Import des sets terminé");
        log.info("Séries traitées : {}", seriesProcessed);
        log.info("Sets créés      : {}", setsImported);
        log.info("Sets mis à jour : {}", setsUpdated);
        log.info("========================================");
        return seriesProcessed;
    }

    private boolean importOrUpdateSet(
            TcgDexSetResponseDto source,
            TcgDexSerieResponseDto serie
    ) {

        PokemonSet pokemonSet =
                pokemonSetRepository
                        .findByApiId(source.getId())
                        .orElse(null);

        boolean created =
                pokemonSet == null;

        if (created) {

            pokemonSet =
                    tcgDexSetMapper.toEntity(
                            source
                    );

        } else {

            /*
             * On met à jour les informations du set.
             */
            pokemonSet.setName(
                    source.getName()
            );

            pokemonSet.setLogoUrl(
                    source.getLogo()
            );


            pokemonSet.setReleaseDate(
                    LocalDate.parse(source.getReleaseDate())
            );
        }

        /*
         * Si ton entité PokemonSet possède une relation
         * vers une entité Serie, on la remplira ici.
         *
         * Si tu n'as pas de relation Serie dans ton entité,
         * cette partie n'est pas nécessaire.
         */

        pokemonSetRepository.save(
                pokemonSet
        );

        log.info(
                "{} set : {} - {}",
                created ? "Créé" : "Mis à jour",
                source.getId(),
                source.getName()
        );

        return created;
    }
}