package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.PeticionesPagoDTO;
import org.davide.ggbproyect.service.PeticionesPagoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Page<PeticionesPagoDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(peticionesPagoService.getAll(pageable));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<PeticionesPagoDTO>> filter(
            @RequestParam(required = false) Integer idSesion,
            @RequestParam(required = false) String metodoPreferido,
            @RequestParam(required = false) Boolean atendida) {
        return ResponseEntity.ok(peticionesPagoService.filter(idSesion, metodoPreferido, atendida));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<PeticionesPagoDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(peticionesPagoService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<PeticionesPagoDTO> create(@Valid @RequestBody PeticionesPagoDTO peticionesPagoDTO) {
        PeticionesPagoDTO created = peticionesPagoService.create(peticionesPagoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PeticionesPagoDTO> update(@PathVariable Integer id, @Valid @RequestBody PeticionesPagoDTO peticionesPagoDTO) {
        return ResponseEntity.ok(peticionesPagoService.update(id, peticionesPagoDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        peticionesPagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
