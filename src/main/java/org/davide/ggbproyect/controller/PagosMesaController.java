package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.PagosMesaDTO;
import org.davide.ggbproyect.service.PagosMesaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos-mesa")
public class PagosMesaController {

    private final PagosMesaService pagosMesaService;

    public PagosMesaController(PagosMesaService pagosMesaService) {
        this.pagosMesaService = pagosMesaService;
    }

    @GetMapping
    public ResponseEntity<List<PagosMesaDTO>> getAll() {
        return ResponseEntity.ok(pagosMesaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagosMesaDTO> getById(@PathVariable Integer id) {
        return pagosMesaService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PagosMesaDTO> create(@Valid @RequestBody PagosMesaDTO pagosMesaDTO) {
        return new ResponseEntity<>(pagosMesaService.create(pagosMesaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagosMesaDTO> update(@PathVariable Integer id, @Valid @RequestBody PagosMesaDTO pagosMesaDTO) {
        return pagosMesaService.update(id, pagosMesaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            if (pagosMesaService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}