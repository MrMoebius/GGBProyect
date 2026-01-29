package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.JuegoDTO;
import org.davide.ggbproyect.models.LineasComandaDTO;
import org.davide.ggbproyect.service.LineasComandaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lineas-comanda")
public class LineasComandaController {

    private final LineasComandaService lineasComandaService;

    public LineasComandaController(LineasComandaService lineasComandaService) {
        this.lineasComandaService = lineasComandaService;
    }

    @GetMapping
    public ResponseEntity<List<LineasComandaDTO>> getAll() {
        return ResponseEntity.ok(lineasComandaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineasComandaDTO> getById(@PathVariable Integer id) {
        return lineasComandaService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LineasComandaDTO> create(@Valid @RequestBody LineasComandaDTO lineasComandaDTO) {
        return new ResponseEntity<>(lineasComandaService.create(lineasComandaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineasComandaDTO> update(@PathVariable Integer id, @Valid @RequestBody LineasComandaDTO lineasComandaDTO) {
        return lineasComandaService.update(id, lineasComandaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            if (lineasComandaService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}