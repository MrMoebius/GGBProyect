package org.davide.ggbproyect.controller;

import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ReservasMesaDTO;
import org.davide.ggbproyect.service.ReservasMesaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas-mesa")
public class ReservasMesaController {

    private final ReservasMesaService reservasMesaService;

    public ReservasMesaController(ReservasMesaService reservasMesaService) {
        this.reservasMesaService = reservasMesaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Page<ReservasMesaDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(reservasMesaService.getAll(pageable));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Page<ReservasMesaDTO>> filter(
            @RequestParam(required = false) Integer idCliente,
            @RequestParam(required = false) Integer idMesa,
            @RequestParam(required = false) Integer idJuegoDeseado,
            @RequestParam(required = false) String estado,
            Pageable pageable) {
        return ResponseEntity.ok(reservasMesaService.filter(idCliente, idMesa, idJuegoDeseado, estado, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<ReservasMesaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(reservasMesaService.getById(id));
    }

    @GetMapping("/mis-reservas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ReservasMesaDTO>> getMisReservas(Authentication auth) {
        return ResponseEntity.ok(reservasMesaService.getMisReservas(auth.getName()));
    }

    @PostMapping("/{id}/cancelar-cliente")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ReservasMesaDTO> cancelarByCliente(@PathVariable Integer id, Authentication auth) {
        return ResponseEntity.ok(reservasMesaService.cancelarByCliente(id, auth.getName()));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<ReservasMesaDTO> changeEstado(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        if (estado == null) throw new IllegalArgumentException("El campo 'estado' es obligatorio");
        return ResponseEntity.ok(reservasMesaService.changeEstado(id, estado));
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<ReservasMesaDTO> create(@Valid @RequestBody ReservasMesaDTO reservasMesaDTO) {
        ReservasMesaDTO created = reservasMesaService.create(reservasMesaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<ReservasMesaDTO> update(@PathVariable Integer id, @Valid @RequestBody ReservasMesaDTO reservasMesaDTO) {
        return ResponseEntity.ok(reservasMesaService.update(id, reservasMesaDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        reservasMesaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
