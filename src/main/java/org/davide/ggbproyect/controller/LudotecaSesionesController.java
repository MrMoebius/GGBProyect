package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.JuegoDTO;
import org.davide.ggbproyect.models.LudotecaSesionesDTO;
import org.davide.ggbproyect.service.LudotecaSesionesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ludoteca-sesiones")
public class LudotecaSesionesController {

    private final LudotecaSesionesService ludotecaSesionesService;

    public LudotecaSesionesController(LudotecaSesionesService ludotecaSesionesService) {
        this.ludotecaSesionesService = ludotecaSesionesService;
    }

    @GetMapping
    public ResponseEntity<List<LudotecaSesionesDTO>> getAll() {
        return ResponseEntity.ok(ludotecaSesionesService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LudotecaSesionesDTO> getById(@PathVariable Integer id) {
        return ludotecaSesionesService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LudotecaSesionesDTO> create(@Valid @RequestBody LudotecaSesionesDTO ludotecaSesionesDTO) {
        return new ResponseEntity<>(ludotecaSesionesService.create(ludotecaSesionesDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LudotecaSesionesDTO> update(@PathVariable Integer id, @Valid @RequestBody LudotecaSesionesDTO ludotecaSesionesDTO) {
        return ludotecaSesionesService.update(id, ludotecaSesionesDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            if (ludotecaSesionesService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}