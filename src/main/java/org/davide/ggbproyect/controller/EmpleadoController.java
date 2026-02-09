package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.EmpleadoDTO;
import org.davide.ggbproyect.service.EmpleadoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@PreAuthorize("hasRole('ADMIN')")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<Page<EmpleadoDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(empleadoService.getAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EmpleadoDTO>> filter(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer idRol,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(empleadoService.filter(nombre, email, idRol, estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(empleadoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<EmpleadoDTO> create(@Valid @RequestBody EmpleadoDTO empleadoDTO) {
        EmpleadoDTO created = empleadoService.create(empleadoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> update(@PathVariable Integer id, @Valid @RequestBody EmpleadoDTO empleadoDTO) {
        return ResponseEntity.ok(empleadoService.update(id, empleadoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
