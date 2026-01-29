package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ReservasMesaDTO;
import org.davide.ggbproyect.service.ReservasMesaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas-mesa")
public class ReservasMesaController {

    private final ReservasMesaService reservasMesaService;

    public ReservasMesaController(ReservasMesaService reservasMesaService) {
        this.reservasMesaService = reservasMesaService;
    }

    @GetMapping
    public ResponseEntity<List<ReservasMesaDTO>> getAll() {
        return ResponseEntity.ok(reservasMesaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservasMesaDTO> getById(@PathVariable Integer id) {
        return reservasMesaService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReservasMesaDTO> create(@Valid @RequestBody ReservasMesaDTO reservasMesaDTO) {
        return new ResponseEntity<>(reservasMesaService.create(reservasMesaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservasMesaDTO> update(@PathVariable Integer id, @Valid @RequestBody ReservasMesaDTO reservasMesaDTO) {
        return reservasMesaService.update(id, reservasMesaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (reservasMesaService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}