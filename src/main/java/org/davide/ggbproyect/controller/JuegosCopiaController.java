package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.JuegosCopiaDTO;
import org.davide.ggbproyect.service.JuegosCopiaService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<JuegosCopiaDTO>> getAll() {
        return ResponseEntity.ok(juegosCopiaService.getAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<JuegosCopiaDTO>> filter(
            @RequestParam(required = false) Integer idJuego,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(juegosCopiaService.filter(idJuego, estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuegosCopiaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(juegosCopiaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<JuegosCopiaDTO> create(@Valid @RequestBody JuegosCopiaDTO juegosCopiaDTO) {
        JuegosCopiaDTO created = juegosCopiaService.create(juegosCopiaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuegosCopiaDTO> update(@PathVariable Integer id, @Valid @RequestBody JuegosCopiaDTO juegosCopiaDTO) {
        return ResponseEntity.ok(juegosCopiaService.update(id, juegosCopiaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        juegosCopiaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
