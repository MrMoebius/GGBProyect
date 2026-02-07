package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ReservasJuegoDTO;
import org.davide.ggbproyect.service.ReservasJuegoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reservas-juego")
public class ReservasJuegoController {

    private final ReservasJuegoService reservasJuegoService;

    public ReservasJuegoController(ReservasJuegoService reservasJuegoService) {
        this.reservasJuegoService = reservasJuegoService;
    }

    @GetMapping
    public ResponseEntity<List<ReservasJuegoDTO>> getAll() {
        return ResponseEntity.ok(reservasJuegoService.getAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ReservasJuegoDTO>> filter(
            @RequestParam(required = false) Integer idSesion,
            @RequestParam(required = false) Integer idCopia,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(reservasJuegoService.filter(idSesion, idCopia, estado));
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
