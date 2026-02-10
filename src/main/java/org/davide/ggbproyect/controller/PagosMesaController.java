package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.PagosMesaDTO;
import org.davide.ggbproyect.service.PagosMesaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pagos-mesa")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
public class PagosMesaController {

    private final PagosMesaService pagosMesaService;

    public PagosMesaController(PagosMesaService pagosMesaService) {
        this.pagosMesaService = pagosMesaService;
    }

    @GetMapping
    public ResponseEntity<Page<PagosMesaDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(pagosMesaService.getAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<PagosMesaDTO>> filter(
            @RequestParam(required = false) Integer idSesion,
            @RequestParam(required = false) String metodoPago,
            @RequestParam(required = false) String estado,
            Pageable pageable) {
        return ResponseEntity.ok(pagosMesaService.filter(idSesion, metodoPago, estado, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagosMesaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(pagosMesaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PagosMesaDTO> create(@Valid @RequestBody PagosMesaDTO pagosMesaDTO) {
        PagosMesaDTO created = pagosMesaService.create(pagosMesaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagosMesaDTO> update(@PathVariable Integer id, @Valid @RequestBody PagosMesaDTO pagosMesaDTO) {
        return ResponseEntity.ok(pagosMesaService.update(id, pagosMesaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pagosMesaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
