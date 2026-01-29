package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ReservasJuegoDTO;
import org.davide.ggbproyect.models.TarifasLudoteca;
import org.davide.ggbproyect.models.TarifasLudotecaDTO;
import org.davide.ggbproyect.service.TarifasLudotecaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarifas-ludoteca")
public class TarifasLudotecaController {

    private final TarifasLudotecaService tarifasLudotecaService;

    public TarifasLudotecaController(TarifasLudotecaService tarifasLudotecaService) {
        this.tarifasLudotecaService = tarifasLudotecaService;
    }

    @GetMapping
    private ResponseEntity<List<TarifasLudotecaDTO>> findALL()
    {
        return ResponseEntity.ok(tarifasLudotecaService.getAll());
    }

    @GetMapping("/{id}")
    private ResponseEntity<TarifasLudotecaDTO> findById(@PathVariable Integer id)
    {
        return tarifasLudotecaService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    private ResponseEntity<TarifasLudotecaDTO> create(@Valid @RequestBody TarifasLudotecaDTO tarifasLudotecaDTO)
    {
        return new ResponseEntity<>(tarifasLudotecaService.create(tarifasLudotecaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    private ResponseEntity<TarifasLudotecaDTO> update(@PathVariable Integer id ,@Valid @RequestBody TarifasLudotecaDTO tarifasLudotecaDTO)
    {
        return tarifasLudotecaService.update(id,tarifasLudotecaDTO).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete( @PathVariable Integer id)
    {
        try {
            if (tarifasLudotecaService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}

