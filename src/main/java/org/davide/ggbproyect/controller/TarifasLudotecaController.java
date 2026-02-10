package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.TarifasLudotecaDTO;
import org.davide.ggbproyect.service.TarifasLudotecaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tarifas-ludoteca")
public class TarifasLudotecaController {

    private final TarifasLudotecaService tarifasLudotecaService;

    public TarifasLudotecaController(TarifasLudotecaService tarifasLudotecaService) {
        this.tarifasLudotecaService = tarifasLudotecaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<Page<TarifasLudotecaDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(tarifasLudotecaService.getAll(pageable));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<Page<TarifasLudotecaDTO>> filter(
            @RequestParam(required = false) String nombreTramo,
            @RequestParam(required = false) Boolean activo,
            Pageable pageable) {
        return ResponseEntity.ok(tarifasLudotecaService.filter(nombreTramo, activo, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<TarifasLudotecaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(tarifasLudotecaService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TarifasLudotecaDTO> create(@Valid @RequestBody TarifasLudotecaDTO tarifasLudotecaDTO) {
        TarifasLudotecaDTO created = tarifasLudotecaService.create(tarifasLudotecaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TarifasLudotecaDTO> update(@PathVariable Integer id, @Valid @RequestBody TarifasLudotecaDTO tarifasLudotecaDTO) {
        return ResponseEntity.ok(tarifasLudotecaService.update(id, tarifasLudotecaDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tarifasLudotecaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
