package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.JuegoDTO;
import org.davide.ggbproyect.service.JuegoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/juegos")
public class JuegoController {

    private final JuegoService juegoService;

    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<Page<JuegoDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(juegoService.getAll(pageable));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<List<JuegoDTO>> filter(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String complejidad,
            @RequestParam(required = false) String idioma,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) Boolean recomendadoDosJugadores) {
        return ResponseEntity.ok(juegoService.filter(nombre, complejidad, idioma, ubicacion, activo, recomendadoDosJugadores));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<JuegoDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(juegoService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JuegoDTO> create(@Valid @RequestBody JuegoDTO juegoDTO) {
        JuegoDTO created = juegoService.create(juegoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JuegoDTO> update(@PathVariable Integer id, @Valid @RequestBody JuegoDTO juegoDTO) {
        return ResponseEntity.ok(juegoService.update(id, juegoDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        juegoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
