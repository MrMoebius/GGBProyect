package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.RolesEmpleadoDTO;
import org.davide.ggbproyect.service.RolesEmpleadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles-empleado")
public class RolesEmpleadoController {

    private final RolesEmpleadoService rolesEmpleadoService;

    public RolesEmpleadoController(RolesEmpleadoService rolesEmpleadoService) {
        this.rolesEmpleadoService = rolesEmpleadoService;
    }

    @GetMapping
    public ResponseEntity<List<RolesEmpleadoDTO>> getAll() {
        return ResponseEntity.ok(rolesEmpleadoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolesEmpleadoDTO> getById(@PathVariable Integer id) {
        return rolesEmpleadoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RolesEmpleadoDTO> create(@Valid @RequestBody RolesEmpleadoDTO rolesEmpleadoDTO) {
        return new ResponseEntity<>(rolesEmpleadoService.create(rolesEmpleadoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolesEmpleadoDTO> update(@PathVariable Integer id, @Valid @RequestBody RolesEmpleadoDTO rolesEmpleadoDTO) {
        return rolesEmpleadoService.update(id, rolesEmpleadoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            if (rolesEmpleadoService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}