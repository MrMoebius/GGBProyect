package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.EventoDTO;
import org.davide.ggbproyect.service.EventoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/eventos")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<EventoDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(eventoService.getAll(pageable));
    }

    @GetMapping("/filter")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<EventoDTO>> filter(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String estado,
            Pageable pageable) {
        return ResponseEntity.ok(eventoService.filter(tipo, estado, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EventoDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(eventoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<EventoDTO> create(@Valid @RequestBody EventoDTO dto) {
        EventoDTO created = eventoService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoDTO> update(@PathVariable Integer id,
                                            @Valid @RequestBody EventoDTO dto) {
        return ResponseEntity.ok(eventoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        eventoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
