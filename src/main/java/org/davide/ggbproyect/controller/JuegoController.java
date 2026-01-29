package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.JuegoDTO;
import org.davide.ggbproyect.service.JuegoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juegos")
public class JuegoController {

    private final JuegoService juegoService;

    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @GetMapping
    public ResponseEntity<List<JuegoDTO>> getAll() {
        return ResponseEntity.ok(juegoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuegoDTO> getById(@PathVariable Integer id) {
        return juegoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<JuegoDTO> create(@Valid @RequestBody JuegoDTO juegoDTO) {
        return new ResponseEntity<>(juegoService.create(juegoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuegoDTO> update(@PathVariable Integer id, @Valid @RequestBody JuegoDTO juegoDTO) {
        return juegoService.update(id, juegoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (juegoService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}