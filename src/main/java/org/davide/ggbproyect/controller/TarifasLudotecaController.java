package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.TarifasLudotecaDTO;
import org.davide.ggbproyect.service.TarifasLudotecaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tarifas-ludoteca")
public class TarifasLudotecaController {

    private final TarifasLudotecaService tarifasLudotecaService;

    public TarifasLudotecaController(TarifasLudotecaService tarifasLudotecaService) {
        this.tarifasLudotecaService = tarifasLudotecaService;
    }

    @GetMapping
    public ResponseEntity<List<TarifasLudotecaDTO>> getAll() {
        return ResponseEntity.ok(tarifasLudotecaService.getAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TarifasLudotecaDTO>> filter(
            @RequestParam(required = false) String nombreTramo,
            @RequestParam(required = false) Boolean activo) {
        return ResponseEntity.ok(tarifasLudotecaService.filter(nombreTramo, activo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarifasLudotecaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(tarifasLudotecaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TarifasLudotecaDTO> create(@Valid @RequestBody TarifasLudotecaDTO tarifasLudotecaDTO) {
        TarifasLudotecaDTO created = tarifasLudotecaService.create(tarifasLudotecaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarifasLudotecaDTO> update(@PathVariable Integer id, @Valid @RequestBody TarifasLudotecaDTO tarifasLudotecaDTO) {
        return ResponseEntity.ok(tarifasLudotecaService.update(id, tarifasLudotecaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tarifasLudotecaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
