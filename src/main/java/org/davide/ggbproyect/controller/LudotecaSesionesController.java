package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.LudotecaSesionesDTO;
import org.davide.ggbproyect.service.LudotecaSesionesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ludoteca-sesiones")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
public class LudotecaSesionesController {

    private final LudotecaSesionesService ludotecaSesionesService;

    public LudotecaSesionesController(LudotecaSesionesService ludotecaSesionesService) {
        this.ludotecaSesionesService = ludotecaSesionesService;
    }

    @GetMapping
    public ResponseEntity<Page<LudotecaSesionesDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(ludotecaSesionesService.getAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<LudotecaSesionesDTO>> filter(
            @RequestParam(required = false) Integer idSesion,
            Pageable pageable) {
        return ResponseEntity.ok(ludotecaSesionesService.filter(idSesion, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LudotecaSesionesDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ludotecaSesionesService.getById(id));
    }

    @PostMapping
    public ResponseEntity<LudotecaSesionesDTO> create(@Valid @RequestBody LudotecaSesionesDTO ludotecaSesionesDTO) {
        LudotecaSesionesDTO created = ludotecaSesionesService.create(ludotecaSesionesDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LudotecaSesionesDTO> update(@PathVariable Integer id, @Valid @RequestBody LudotecaSesionesDTO ludotecaSesionesDTO) {
        return ResponseEntity.ok(ludotecaSesionesService.update(id, ludotecaSesionesDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ludotecaSesionesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
