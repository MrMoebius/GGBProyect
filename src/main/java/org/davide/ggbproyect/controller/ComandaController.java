package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ComandaDTO;
import org.davide.ggbproyect.service.ComandaService;
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
@RequestMapping("/api/comandas")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
public class ComandaController {

    private final ComandaService comandaService;

    public ComandaController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Page<ComandaDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(comandaService.getAll(pageable));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Page<ComandaDTO>> filter(
            @RequestParam(required = false) Integer idSesion,
            @RequestParam(required = false) String estado,
            Pageable pageable) {
        return ResponseEntity.ok(comandaService.filter(idSesion, estado, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComandaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(comandaService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
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

    @PostMapping("/cliente")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ComandaDTO> createByCliente(@Valid @RequestBody ComandaDTO comandaDTO, Authentication auth) {
        ComandaDTO created = comandaService.createByCliente(comandaDTO, auth.getName());
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}").buildAndExpand(created.getId()).toUri()
        ).body(created);
    }

    @PostMapping("/{id}/confirmar")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<ComandaDTO> confirmar(@PathVariable Integer id) {
        return ResponseEntity.ok(comandaService.confirmar(id));
    }

    @PostMapping("/{id}/preparar")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<ComandaDTO> preparar(@PathVariable Integer id) {
        return ResponseEntity.ok(comandaService.preparar(id));
    }

    @PostMapping("/{id}/servir")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<ComandaDTO> servir(@PathVariable Integer id) {
        return ResponseEntity.ok(comandaService.servir(id));
    }

    @PostMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<ComandaDTO> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(comandaService.cancelar(id));
    }

    @GetMapping("/mis-comandas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ComandaDTO>> getMisComandas(Authentication auth) {
        return ResponseEntity.ok(comandaService.getMisComandasBySesion(auth.getName()));
    }

    @PostMapping("/{id}/cancelar-cliente")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ComandaDTO> cancelarByCliente(@PathVariable Integer id, Authentication auth) {
        return ResponseEntity.ok(comandaService.cancelarByCliente(id, auth.getName()));
    }

    @GetMapping("/sesion/{idSesion}/cliente")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ComandaDTO>> getBySesionCliente(@PathVariable Integer idSesion, Authentication auth) {
        return ResponseEntity.ok(comandaService.getBySesionCliente(idSesion, auth.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        comandaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
