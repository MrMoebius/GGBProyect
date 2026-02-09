package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ComandaDTO;
import org.davide.ggbproyect.service.ComandaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/comandas")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
public class ComandaController {

    private final ComandaService comandaService;

    public ComandaController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    @GetMapping
    public ResponseEntity<Page<ComandaDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(comandaService.getAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ComandaDTO>> filter(
            @RequestParam(required = false) Integer idSesion,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(comandaService.filter(idSesion, estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComandaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(comandaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ComandaDTO> create(@Valid @RequestBody ComandaDTO comandaDTO) {
        ComandaDTO created = comandaService.create(comandaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComandaDTO> update(@PathVariable Integer id, @Valid @RequestBody ComandaDTO comandaDTO) {
        return ResponseEntity.ok(comandaService.update(id, comandaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        comandaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
