package com.pokemon.mypokecardex.service.external;

import com.pokemon.mypokecardex.client.TcgDexClient;
import com.pokemon.mypokecardex.dto.external.*;
import com.pokemon.mypokecardex.entity.catalogue.*;
import com.pokemon.mypokecardex.mapper.TcgDexCardMapper;
import com.pokemon.mypokecardex.repository.catalogue.PokemonCardRepository;
import com.pokemon.mypokecardex.repository.catalogue.PokemonSetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TcgDexCardImportService {

    private final TcgDexClient tcgDexClient;

    private final PokemonSetRepository pokemonSetRepository;
    private final PokemonCardRepository pokemonCardRepository;
    private final TcgDexCardTransactionService cardTransactionService;

    private final TcgDexCardMapper tcgDexCardMapper;


    public int importCardsFromSet(String setId) {

        log.info("========================================");
        log.info("Début import cartes du set {}", setId);
        log.info("========================================");

        PokemonSet pokemonSet = pokemonSetRepository
                .findByApiId(setId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Set introuvable en base : " + setId
                        )
                );

        TcgDexSetResponseDto set =
                tcgDexClient.getSet(setId);

        if (set == null || set.getCards() == null) {
            log.warn("Aucune carte trouvée pour le set {}", setId);
            return 0;
        }

        int total = set.getCards().size();
        int processed = 0;
        int cardsCreated = 0;
        int printsCreated = 0;
        int errors = 0;

        log.info(
                "{} cartes trouvées dans {}",
                total,
                setId
        );

        for (var cardBrief : set.getCards()) {

            processed++;

            log.info(
                    "[{}/{}] {} - {}",
                    processed,
                    total,
                    cardBrief.getId(),
                    cardBrief.getName()
            );

            try {

                TcgDexCardResponseDto card =
                        tcgDexClient.getCard(
                                setId,
                                cardBrief.getLocalId()
                        );

                if (card == null) {
                    log.warn(
                            "Détail introuvable pour {}",
                            cardBrief.getId()
                    );
                    errors++;
                    continue;
                }

                cardTransactionService.saveCard(
                        card,
                        pokemonSet
                );

                cardsCreated++;

            } catch (Exception e) {

                errors++;

                log.error(
                        "Erreur import carte {}",
                        cardBrief.getId(),
                        e
                );
            }
        }

        log.info("========================================");
        log.info("Import terminé pour {}", setId);
        log.info("Cartes traitées : {}", processed);
        log.info("PokemonCard créées : {}", cardsCreated);
        log.info("CardPrint créés : {}", printsCreated);
        log.info("Erreurs : {}", errors);
        log.info("========================================");
        return cardsCreated;
    }


    public int importCardsFromAllSets() {

        log.info("========================================");
        log.info("Début import des cartes de tous les sets");
        log.info("========================================");

        List<PokemonSet> pokemonSets =
                pokemonSetRepository.findAll();

        if (pokemonSets.isEmpty()) {

            log.warn("Aucun set présent en base");

            return 0;
        }

        int totalCardsCreated = 0;

        log.info(
                "{} sets trouvés en base",
                pokemonSets.size()
        );

        for (int i = 0; i < pokemonSets.size(); i++) {

            PokemonSet pokemonSet =
                    pokemonSets.get(i);

            log.info(
                    "===== SET [{}/{}] : {} - {} =====",
                    i + 1,
                    pokemonSets.size(),
                    pokemonSet.getApiId(),
                    pokemonSet.getName()
            );

            try {

                int cardsCreated =
                        importCardsFromSet(
                                pokemonSet.getApiId()
                        );

                totalCardsCreated += cardsCreated;

            } catch (Exception e) {

                log.error(
                        "Erreur lors de l'import du set {}",
                        pokemonSet.getApiId(),
                        e
                );
            }
        }

        log.info("========================================");
        log.info("Import de tous les sets terminé");
        log.info(
                "Sets traités : {}",
                pokemonSets.size()
        );
        log.info(
                "PokemonCard créées : {}",
                totalCardsCreated
        );
        log.info("========================================");

        return totalCardsCreated;
    }

}