package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.models.InscripcionEventoDTO;
import org.davide.ggbproyect.service.InscripcionEventoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InscripcionEventoController {

    private final InscripcionEventoService inscripcionService;

    public InscripcionEventoController(InscripcionEventoService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping("/api/eventos/{id}/inscribirse")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<InscripcionEventoDTO> inscribirse(@PathVariable Integer id,
                                                            Authentication auth) {
        return ResponseEntity.ok(inscripcionService.inscribirse(id, auth.getName()));
    }

    @PostMapping("/api/eventos/{id}/desinscribirse")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> desinscribirse(@PathVariable Integer id,
                                               Authentication auth) {
        inscripcionService.desinscribirse(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/eventos/{id}/inscripciones")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<InscripcionEventoDTO>> getByEvento(@PathVariable Integer id) {
        return ResponseEntity.ok(inscripcionService.getByEvento(id));
    }

    @GetMapping("/api/inscripciones/mis-inscripciones")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<InscripcionEventoDTO>> getMisInscripciones(Authentication auth) {
        return ResponseEntity.ok(inscripcionService.getMisInscripciones(auth.getName()));
    }
}
