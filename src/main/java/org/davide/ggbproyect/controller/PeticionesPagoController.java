package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.PeticionesPagoDTO;
import org.davide.ggbproyect.service.PeticionesPagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/peticiones-pago")
public class PeticionesPagoController {

    private final PeticionesPagoService peticionesPagoService;

    public PeticionesPagoController(PeticionesPagoService peticionesPagoService) {
        this.peticionesPagoService = peticionesPagoService;
    }

    @GetMapping
    public ResponseEntity<List<PeticionesPagoDTO>> getAll() {
        return ResponseEntity.ok(peticionesPagoService.getAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PeticionesPagoDTO>> filter(
            @RequestParam(required = false) Integer idSesion,
            @RequestParam(required = false) String metodoPreferido,
            @RequestParam(required = false) Boolean atendida) {
        return ResponseEntity.ok(peticionesPagoService.filter(idSesion, metodoPreferido, atendida));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeticionesPagoDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(peticionesPagoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PeticionesPagoDTO> create(@Valid @RequestBody PeticionesPagoDTO peticionesPagoDTO) {
        PeticionesPagoDTO created = peticionesPagoService.create(peticionesPagoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeticionesPagoDTO> update(@PathVariable Integer id, @Valid @RequestBody PeticionesPagoDTO peticionesPagoDTO) {
        return ResponseEntity.ok(peticionesPagoService.update(id, peticionesPagoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        peticionesPagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
