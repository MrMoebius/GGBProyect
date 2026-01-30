package org.davide.ggbproyect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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