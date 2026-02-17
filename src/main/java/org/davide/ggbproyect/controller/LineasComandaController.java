package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.LineasComandaDTO;
import org.davide.ggbproyect.service.LineasComandaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lineas-comanda")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
public class LineasComandaController {

    private final LineasComandaService lineasComandaService;

    public LineasComandaController(LineasComandaService lineasComandaService) {
        this.lineasComandaService = lineasComandaService;
    }

    @GetMapping
    public ResponseEntity<Page<LineasComandaDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(lineasComandaService.getAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<LineasComandaDTO>> filter(
            @RequestParam(required = false) Integer idComanda,
            @RequestParam(required = false) Integer idProducto,
            Pageable pageable) {
        return ResponseEntity.ok(lineasComandaService.filter(idComanda, idProducto, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineasComandaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(lineasComandaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<LineasComandaDTO> create(@Valid @RequestBody LineasComandaDTO lineasComandaDTO) {
        LineasComandaDTO created = lineasComandaService.create(lineasComandaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineasComandaDTO> update(@PathVariable Integer id, @Valid @RequestBody LineasComandaDTO lineasComandaDTO) {
        return ResponseEntity.ok(lineasComandaService.update(id, lineasComandaDTO));
    }

    @GetMapping("/comanda/{idComanda}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<List<LineasComandaDTO>> getByComanda(@PathVariable Integer idComanda) {
        return ResponseEntity.ok(lineasComandaService.getByComandaId(idComanda));
    }

    @PostMapping("/cliente")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<LineasComandaDTO> createByCliente(@Valid @RequestBody LineasComandaDTO dto, Authentication auth) {
        LineasComandaDTO created = lineasComandaService.createByCliente(dto, auth.getName());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{id}/cliente")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> deleteByCliente(@PathVariable Integer id, Authentication auth) {
        lineasComandaService.deleteByCliente(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        lineasComandaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
