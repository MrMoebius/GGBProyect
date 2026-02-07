package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.SesionesMesaDTO;
import org.davide.ggbproyect.service.SesionesMesaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sesiones-mesa")
public class SesionesMesaController {

    private final SesionesMesaService sesionesMesaService;

    public SesionesMesaController(SesionesMesaService sesionesMesaService) {
        this.sesionesMesaService = sesionesMesaService;
    }

    @GetMapping
    public ResponseEntity<List<SesionesMesaDTO>> getAll() {
        return ResponseEntity.ok(sesionesMesaService.getAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<SesionesMesaDTO>> filter(
            @RequestParam(required = false) Integer idMesa,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer idReserva,
            @RequestParam(required = false) Integer idEmpleadoApertura) {
        return ResponseEntity.ok(sesionesMesaService.filter(idMesa, estado, idReserva, idEmpleadoApertura));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SesionesMesaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(sesionesMesaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SesionesMesaDTO> create(@Valid @RequestBody SesionesMesaDTO sesionesMesaDTO) {
        SesionesMesaDTO created = sesionesMesaService.create(sesionesMesaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SesionesMesaDTO> update(@PathVariable Integer id, @Valid @RequestBody SesionesMesaDTO sesionesMesaDTO) {
        return ResponseEntity.ok(sesionesMesaService.update(id, sesionesMesaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sesionesMesaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
