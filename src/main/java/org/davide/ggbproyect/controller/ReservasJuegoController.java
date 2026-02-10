package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ReservasJuegoDTO;
import org.davide.ggbproyect.service.ReservasJuegoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reservas-juego")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
public class ReservasJuegoController {

    private final ReservasJuegoService reservasJuegoService;

    public ReservasJuegoController(ReservasJuegoService reservasJuegoService) {
        this.reservasJuegoService = reservasJuegoService;
    }

    @GetMapping
    public ResponseEntity<Page<ReservasJuegoDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(reservasJuegoService.getAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ReservasJuegoDTO>> filter(
            @RequestParam(required = false) Integer idSesion,
            @RequestParam(required = false) Integer idCopia,
            @RequestParam(required = false) String estado,
            Pageable pageable) {
        return ResponseEntity.ok(reservasJuegoService.filter(idSesion, idCopia, estado, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservasJuegoDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(reservasJuegoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ReservasJuegoDTO> create(@Valid @RequestBody ReservasJuegoDTO reservasJuegoDTO) {
        ReservasJuegoDTO created = reservasJuegoService.create(reservasJuegoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservasJuegoDTO> update(@PathVariable Integer id, @Valid @RequestBody ReservasJuegoDTO reservasJuegoDTO) {
        return ResponseEntity.ok(reservasJuegoService.update(id, reservasJuegoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        reservasJuegoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
