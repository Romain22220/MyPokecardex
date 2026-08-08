package com.pokemon.mypokecardex.controller;

import com.pokemon.mypokecardex.service.external.TcgDexImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/import")
@RequiredArgsConstructor
public class TcgDexImportController {

    private final TcgDexImportService tcgDexImportService;

    @PostMapping("/sets")
    public ResponseEntity<ImportResponse> importSets() {

        int imported =
                tcgDexImportService.importPhysicalSets();

        return ResponseEntity.ok(
                new ImportResponse(
                        imported,
                        "Import des sets physiques terminé"
                )
        );
    }

    public record ImportResponse(
            int imported,
            String message
    ) {
    }
}
