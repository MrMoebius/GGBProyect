package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.models.BggGameDetailsDTO;
import org.davide.ggbproyect.models.BggSearchResultDTO;
import org.davide.ggbproyect.service.BggService;
import org.davide.ggbproyect.service.JuegoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bgg")
@PreAuthorize("hasRole('ADMIN')")
public class BggController {

    private final BggService bggService;
    private final JuegoService juegoService;

    public BggController(BggService bggService, JuegoService juegoService) {
        this.bggService = bggService;
        this.juegoService = juegoService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<BggSearchResultDTO>> search(@RequestParam String query) {
        if (query == null || query.trim().length() < 2) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bggService.search(query.trim()));
    }

    @GetMapping("/details/{bggId}")
    public ResponseEntity<BggGameDetailsDTO> getDetails(@PathVariable Integer bggId) {
        return ResponseEntity.ok(bggService.getDetails(bggId));
    }

    @PostMapping("/import-image/{bggId}/{juegoId}")
    public ResponseEntity<?> importImage(@PathVariable Integer bggId, @PathVariable Integer juegoId) {
        juegoService.getById(juegoId);
        bggService.downloadImage(bggId, juegoId);
        return ResponseEntity.ok(Map.of("message", "Imagen importada correctamente"));
    }
}
