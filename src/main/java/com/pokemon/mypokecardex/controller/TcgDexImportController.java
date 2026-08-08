package com.pokemon.mypokecardex.controller;

import com.pokemon.mypokecardex.service.external.TcgDexCardImportService;
import com.pokemon.mypokecardex.service.external.TcgDexImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/import")
@RequiredArgsConstructor
public class TcgDexImportController {

    private final TcgDexImportService tcgDexImportService;

    private final TcgDexCardImportService tcgDexCardImportService;


    @PostMapping("/sets")
    public ResponseEntity<ImportResponse> importSets() {

        int imported = tcgDexImportService.importPhysicalSets();

        return ResponseEntity.ok(
                new ImportResponse(imported, "Import des sets physiques terminé"));
    }


    @PostMapping("/sets/{setId}/cards")
    public ResponseEntity<ImportResponse> importCards(@PathVariable String setId) {

        int imported = tcgDexCardImportService.importCardsFromSet(setId);

        return ResponseEntity.ok(
                new ImportResponse(imported, "Import des cartes du set terminé")
        );
    }


    @PostMapping("/cards")
    public ResponseEntity<ImportResponse> importAllCards() {

        int imported = tcgDexCardImportService.importCardsFromAllSets();

        return ResponseEntity.ok(
                new ImportResponse(imported, "Import des cartes de tous les sets terminé")
        );
    }


    public record ImportResponse(int imported, String message) {
    }
}
