package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ReservasMesaDTO;
import org.davide.ggbproyect.service.ReservasMesaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping("/filter")
    public ResponseEntity<List<ReservasMesaDTO>> filter(
            @RequestParam(required = false) Integer idCliente,
            @RequestParam(required = false) Integer idMesa,
            @RequestParam(required = false) Integer idJuegoDeseado,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(reservasMesaService.filter(idCliente, idMesa, idJuegoDeseado, estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservasMesaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(reservasMesaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ReservasMesaDTO> create(@Valid @RequestBody ReservasMesaDTO reservasMesaDTO) {
        ReservasMesaDTO created = reservasMesaService.create(reservasMesaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservasMesaDTO> update(@PathVariable Integer id, @Valid @RequestBody ReservasMesaDTO reservasMesaDTO) {
        return ResponseEntity.ok(reservasMesaService.update(id, reservasMesaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        reservasMesaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
