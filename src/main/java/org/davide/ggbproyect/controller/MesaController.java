package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.MesaDTO;
import org.davide.ggbproyect.service.MesaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @GetMapping
    public ResponseEntity<List<MesaDTO>> getAll() {
        return ResponseEntity.ok(mesaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MesaDTO> getById(@PathVariable Integer id) {
        return mesaService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MesaDTO> create(@Valid @RequestBody MesaDTO mesaDTO) {
        return new ResponseEntity<>(mesaService.create(mesaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MesaDTO> update(@PathVariable Integer id, @Valid @RequestBody MesaDTO mesaDTO) {
        return mesaService.update(id, mesaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            if (mesaService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}