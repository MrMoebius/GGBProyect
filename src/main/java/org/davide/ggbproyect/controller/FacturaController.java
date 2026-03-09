package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.models.Cliente;
import org.davide.ggbproyect.models.FacturaDTO;
import org.davide.ggbproyect.repository.ClienteRepository;
import org.davide.ggbproyect.service.FacturaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;
    private final ClienteRepository clienteRepository;

    public FacturaController(FacturaService facturaService, ClienteRepository clienteRepository) {
        this.facturaService = facturaService;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Page<FacturaDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(facturaService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<FacturaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(facturaService.getById(id));
    }

    @GetMapping("/sesion/{idSesion}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<FacturaDTO> getBySesionId(@PathVariable Integer idSesion) {
        return ResponseEntity.ok(facturaService.getBySesionId(idSesion));
    }

    @PostMapping("/{id}/enviar-email")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Void> enviarPorEmail(@PathVariable Integer id) {
        facturaService.enviarPorEmail(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mis-facturas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<FacturaDTO>> getMisFacturas(Authentication auth) {
        Cliente cliente = clienteRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        return ResponseEntity.ok(facturaService.getByClienteId(cliente.getId()));
    }
}
