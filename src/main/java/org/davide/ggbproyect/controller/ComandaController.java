package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ComandaDTO;
import org.davide.ggbproyect.service.ComandaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comandas")
public class ComandaController {

    private final ComandaService comandaService;

    public ComandaController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    @GetMapping
    public ResponseEntity<List<ComandaDTO>> getAll() {
        return ResponseEntity.ok(comandaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComandaDTO> getById(@PathVariable Integer id) {
        return comandaService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ComandaDTO> create(@Valid @RequestBody ComandaDTO comandaDTO) {
        return new ResponseEntity<>(comandaService.create(comandaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComandaDTO> update(@PathVariable Integer id, @Valid @RequestBody ComandaDTO comandaDTO) {
        return comandaService.update(id, comandaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (comandaService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}