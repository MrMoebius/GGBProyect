package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.JuegosCopiaDTO;
import org.davide.ggbproyect.service.JuegosCopiaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/juegos-copia")
public class JuegosCopiaController {

    private final JuegosCopiaService juegosCopiaService;

    public JuegosCopiaController(JuegosCopiaService juegosCopiaService) {
        this.juegosCopiaService = juegosCopiaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Page<JuegosCopiaDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(juegosCopiaService.getAll(pageable));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<JuegosCopiaDTO>> filter(
            @RequestParam(required = false) Integer idJuego,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(juegosCopiaService.filter(idJuego, estado));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<JuegosCopiaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(juegosCopiaService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JuegosCopiaDTO> create(@Valid @RequestBody JuegosCopiaDTO juegosCopiaDTO) {
        JuegosCopiaDTO created = juegosCopiaService.create(juegosCopiaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JuegosCopiaDTO> update(@PathVariable Integer id, @Valid @RequestBody JuegosCopiaDTO juegosCopiaDTO) {
        return ResponseEntity.ok(juegosCopiaService.update(id, juegosCopiaDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        juegosCopiaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
