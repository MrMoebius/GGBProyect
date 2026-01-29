package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.JuegoDTO;
import org.davide.ggbproyect.models.JuegosCopiaDTO;
import org.davide.ggbproyect.service.JuegosCopiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juegos-copia")
public class JuegosCopiaController {

    private final JuegosCopiaService juegosCopiaService;

    public JuegosCopiaController(JuegosCopiaService juegosCopiaService) {
        this.juegosCopiaService = juegosCopiaService;
    }

    @GetMapping
    public ResponseEntity<List<JuegosCopiaDTO>> getAll() {
        return ResponseEntity.ok(juegosCopiaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuegosCopiaDTO> getById(@PathVariable Integer id) {
        return juegosCopiaService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<JuegosCopiaDTO> create(@Valid @RequestBody JuegosCopiaDTO juegosCopiaDTO) {
        return new ResponseEntity<>(juegosCopiaService.create(juegosCopiaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuegosCopiaDTO> update(@PathVariable Integer id, @Valid @RequestBody JuegosCopiaDTO juegosCopiaDTO) {
        return juegosCopiaService.update(id, juegosCopiaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            if (juegosCopiaService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}