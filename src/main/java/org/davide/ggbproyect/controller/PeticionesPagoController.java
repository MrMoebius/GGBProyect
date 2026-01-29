package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.PeticionesPago;
import org.davide.ggbproyect.models.PeticionesPagoDTO;
import org.davide.ggbproyect.service.PeticionesPagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/peticiones-pago")
public class PeticionesPagoController {

    private final PeticionesPagoService peticionesPagoService;

    public PeticionesPagoController(PeticionesPagoService peticionesPagoService) {
        this.peticionesPagoService = peticionesPagoService;
    }

    @GetMapping()
    public ResponseEntity<List<PeticionesPagoDTO>> findAll()
    {
        return ResponseEntity.ok(peticionesPagoService.getAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<PeticionesPagoDTO> findById(@PathVariable Integer id)
    {
        return peticionesPagoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping()
    public ResponseEntity<PeticionesPagoDTO> create(@Valid @RequestBody PeticionesPagoDTO peticionesPagoDTO)
    {
        return new ResponseEntity<>(peticionesPagoService.create(peticionesPagoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeticionesPagoDTO> update(@PathVariable Integer id, @RequestBody PeticionesPagoDTO peticionesPagoDTO)
    {
        return peticionesPagoService.update(id,peticionesPagoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id)
    {
        try {
            if (peticionesPagoService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}