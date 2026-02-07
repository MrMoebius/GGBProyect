package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.MesaDTO;
import org.davide.ggbproyect.service.MesaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping("/filter")
    public ResponseEntity<List<MesaDTO>> filter(
            @RequestParam(required = false) String nombreMesa,
            @RequestParam(required = false) String zona,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer capacidad) {
        return ResponseEntity.ok(mesaService.filter(nombreMesa, zona, ubicacion, estado, capacidad));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MesaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(mesaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<MesaDTO> create(@Valid @RequestBody MesaDTO mesaDTO) {
        MesaDTO created = mesaService.create(mesaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MesaDTO> update(@PathVariable Integer id, @Valid @RequestBody MesaDTO mesaDTO) {
        return ResponseEntity.ok(mesaService.update(id, mesaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        mesaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
