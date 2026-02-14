package org.davide.ggbproyect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.davide.ggbproyect.models.SesionesMesaDTO;
import org.davide.ggbproyect.service.SesionesMesaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sesiones-mesa")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
public class SesionesMesaController {

    private final SesionesMesaService sesionesMesaService;

    public SesionesMesaController(SesionesMesaService sesionesMesaService) {
        this.sesionesMesaService = sesionesMesaService;
    }

    @GetMapping
    public ResponseEntity<Page<SesionesMesaDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(sesionesMesaService.getAll(pageable));
    }
    @Operation(summary = "Buscar sesión de mesa por id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sesión encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SesionesMesaDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sesión no encontrada"
            )
    })

    @GetMapping("/filter")
    public ResponseEntity<Page<SesionesMesaDTO>> filter(
            @RequestParam(required = false) Integer idMesa,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer idReserva,
            @RequestParam(required = false) Integer idEmpleadoApertura,
            Pageable pageable) {
        return ResponseEntity.ok(sesionesMesaService.filter(idMesa, estado, idReserva, idEmpleadoApertura, pageable));
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

    @PostMapping("/abrir")
    @Operation(summary = "Abrir sesion de mesa con cascadas automaticas")
    public ResponseEntity<SesionesMesaDTO> abrir(@Valid @RequestBody SesionesMesaDTO sesionesMesaDTO) {
        SesionesMesaDTO created = sesionesMesaService.abrir(sesionesMesaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/{id}/cerrar")
    @Operation(summary = "Cerrar sesion de mesa con cascadas automaticas")
    public ResponseEntity<SesionesMesaDTO> cerrar(@PathVariable Integer id) {
        return ResponseEntity.ok(sesionesMesaService.cerrar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sesionesMesaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
