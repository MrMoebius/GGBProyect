package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ReservasJuegoDTO;
import org.davide.ggbproyect.service.ReservasJuegoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas-juego")
public class ReservasJuegoController {

    private final ReservasJuegoService reservasJuegoService;

    public ReservasJuegoController(ReservasJuegoService reservasJuegoService) {
        this.reservasJuegoService = reservasJuegoService;
    }

    @GetMapping
    public ResponseEntity<List<ReservasJuegoDTO>> getReservasJuego()
    {
        return ResponseEntity.ok(reservasJuegoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservasJuegoDTO> getReservasJuegoById(@PathVariable Integer id)
    {
        return reservasJuegoService.getById(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReservasJuegoDTO> create(@Valid @RequestBody ReservasJuegoDTO reservasJuegoDTO)
    {
        return new ResponseEntity<>(reservasJuegoService.create(reservasJuegoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservasJuegoDTO> update(@PathVariable Integer id, @RequestBody ReservasJuegoDTO reservasJuegoDTO)
    {
        return reservasJuegoService.update(id,reservasJuegoDTO).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id)
    {
        try {
            if (reservasJuegoService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}