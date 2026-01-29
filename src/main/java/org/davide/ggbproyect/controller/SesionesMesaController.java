package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.SesionesMesaDTO;
import org.davide.ggbproyect.service.SesionesMesaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sesiones-mesa")
public class SesionesMesaController {

    private final SesionesMesaService sesionesMesaService;

    public SesionesMesaController(SesionesMesaService sesionesMesaService) {
        this.sesionesMesaService = sesionesMesaService;
    }

    @GetMapping
    public ResponseEntity<List<SesionesMesaDTO>> findAll()
    {
        return ResponseEntity.ok(sesionesMesaService.getAll());

    }

    @GetMapping("/{id}") ResponseEntity<SesionesMesaDTO>  findById(@PathVariable Integer id)
    {
        return sesionesMesaService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping ResponseEntity<SesionesMesaDTO> create(@RequestBody SesionesMesaDTO sesionesMesaDTO)
    {
        return new ResponseEntity<>(sesionesMesaService.create(sesionesMesaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}") ResponseEntity<SesionesMesaDTO> update (@PathVariable Integer id, @Valid  SesionesMesaDTO sesionesMesaDTO)
    {
        return sesionesMesaService.update(id,sesionesMesaDTO).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id)
    {
        try {
            if (sesionesMesaService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}