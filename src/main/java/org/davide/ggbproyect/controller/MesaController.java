package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.LayoutDTO;
import org.davide.ggbproyect.models.MesaDTO;
import org.davide.ggbproyect.service.MesaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<Page<MesaDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(mesaService.getAll(pageable));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<Page<MesaDTO>> filter(
            @RequestParam(required = false) String nombreMesa,
            @RequestParam(required = false) String zona,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer capacidad,
            Pageable pageable) {
        return ResponseEntity.ok(mesaService.filter(nombreMesa, zona, ubicacion, estado, capacidad, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<MesaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(mesaService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MesaDTO> create(@Valid @RequestBody MesaDTO mesaDTO) {
        MesaDTO created = mesaService.create(mesaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MesaDTO> update(@PathVariable Integer id, @Valid @RequestBody MesaDTO mesaDTO) {
        return ResponseEntity.ok(mesaService.update(id, mesaDTO));
    }

    @PutMapping("/layout")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateLayout(@RequestBody List<LayoutDTO> layouts) {
        mesaService.updateLayout(layouts);
        return ResponseEntity.ok(java.util.Map.of("message", "Layout actualizado correctamente"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        mesaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
