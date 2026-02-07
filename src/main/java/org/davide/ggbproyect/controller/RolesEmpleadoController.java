package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.RolesEmpleadoDTO;
import org.davide.ggbproyect.service.RolesEmpleadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping("/filter")
    public ResponseEntity<List<RolesEmpleadoDTO>> filter(
            @RequestParam(required = false) String nombreRol) {
        return ResponseEntity.ok(rolesEmpleadoService.filter(nombreRol));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolesEmpleadoDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(rolesEmpleadoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<RolesEmpleadoDTO> create(@Valid @RequestBody RolesEmpleadoDTO rolesEmpleadoDTO) {
        RolesEmpleadoDTO created = rolesEmpleadoService.create(rolesEmpleadoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolesEmpleadoDTO> update(@PathVariable Integer id, @Valid @RequestBody RolesEmpleadoDTO rolesEmpleadoDTO) {
        return ResponseEntity.ok(rolesEmpleadoService.update(id, rolesEmpleadoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        rolesEmpleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
