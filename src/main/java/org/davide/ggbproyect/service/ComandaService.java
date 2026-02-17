package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Comanda;
import org.davide.ggbproyect.models.ComandaDTO;
import org.davide.ggbproyect.models.Cliente;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.enums.EstadoComanda;
import org.davide.ggbproyect.models.enums.EstadoSesion;
import org.davide.ggbproyect.repository.ClienteRepository;
import org.davide.ggbproyect.repository.ComandaRepository;
import org.davide.ggbproyect.repository.SesionesMesaRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComandaService {

    private final ComandaRepository comandaRepository;
    private final SesionesMesaRepository sesionesMesaRepository;
    private final ClienteRepository clienteRepository;

    private static final Map<EstadoComanda, Set<EstadoComanda>> TRANSICIONES_COMANDA = Map.of(
        EstadoComanda.PENDIENTE, Set.of(EstadoComanda.CONFIRMADA, EstadoComanda.CANCELADA),
        EstadoComanda.CONFIRMADA, Set.of(EstadoComanda.PREPARACION, EstadoComanda.CANCELADA),
        EstadoComanda.PREPARACION, Set.of(EstadoComanda.SERVIDA),
        EstadoComanda.SERVIDA, Set.of(EstadoComanda.PAGADA),
        EstadoComanda.PAGADA, Set.of(),
        EstadoComanda.CANCELADA, Set.of()
    );

    public ComandaService(ComandaRepository comandaRepository,
                          SesionesMesaRepository sesionesMesaRepository,
                          ClienteRepository clienteRepository) {
        this.comandaRepository = comandaRepository;
        this.sesionesMesaRepository = sesionesMesaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public Page<ComandaDTO> getAll(Pageable pageable) {
        return comandaRepository.findAll(pageable)
                .map(ComandaDTO::new);
    }

    @Transactional(readOnly = true)
    public ComandaDTO getById(Integer id) {
        return comandaRepository.findById(id)
                .map(ComandaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + id + " no encontrada"));
    }

    public ComandaDTO create(ComandaDTO comandaDTO) {
        Comanda comanda = comandaDTO.toEntity();
        if (comandaDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(comandaDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + comandaDTO.getIdSesion() + " no encontrada"));
            if (sesion.getEstado() != EstadoSesion.ACTIVA) {
                throw new IllegalStateException("No se pueden crear comandas en sesiones no activas");
            }
            comanda.setIdSesion(sesion);
        }
        return new ComandaDTO(comandaRepository.save(comanda));
    }

    public ComandaDTO update(Integer id, ComandaDTO comandaDTO) {
        Comanda existingComanda = comandaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + id + " no encontrada"));
        if (comandaDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(comandaDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + comandaDTO.getIdSesion() + " no encontrada"));
            existingComanda.setIdSesion(sesion);
        }
        existingComanda.setFechaHora(comandaDTO.getFechaHora());
        if (comandaDTO.getEstado() != null) {
            try {
                EstadoComanda nuevoEstado = EstadoComanda.valueOf(comandaDTO.getEstado());
                validateTransicionComanda(existingComanda.getEstado(), nuevoEstado);
                existingComanda.setEstado(nuevoEstado);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        existingComanda.setTotal(comandaDTO.getTotal());
        return new ComandaDTO(comandaRepository.save(existingComanda));
    }

    private void validateTransicionComanda(EstadoComanda actual, EstadoComanda nuevo) {
        if (actual == nuevo) return;
        Set<EstadoComanda> permitidos = TRANSICIONES_COMANDA.get(actual);
        if (permitidos == null || !permitidos.contains(nuevo)) {
            throw new IllegalStateException(
                "Transicion de estado no permitida: " + actual + " -> " + nuevo);
        }
    }

    public ComandaDTO createByCliente(ComandaDTO comandaDTO, String emailCliente) {
        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new EntityNotFoundException("Cliente con email " + emailCliente + " no encontrado"));

        SesionesMesa sesion = sesionesMesaRepository.findById(comandaDTO.getIdSesion())
                .orElseThrow(() -> new EntityNotFoundException("Sesion con id " + comandaDTO.getIdSesion() + " no encontrada"));

        if (sesion.getEstado() != EstadoSesion.ACTIVA) {
            throw new IllegalStateException("No se pueden crear comandas en sesiones no activas");
        }

        if (sesion.getIdCliente() == null || !sesion.getIdCliente().getId().equals(cliente.getId())) {
            throw new IllegalStateException("No tienes permiso para crear comandas en esta sesion");
        }

        Comanda comanda = new Comanda();
        comanda.setIdSesion(sesion);
        comanda.setEstado(EstadoComanda.PENDIENTE);
        comanda.setFechaHora(java.time.Instant.now());
        comanda.setTotal(BigDecimal.ZERO);

        return new ComandaDTO(comandaRepository.save(comanda));
    }

    public ComandaDTO confirmar(Integer id) {
        return cambiarEstado(id, EstadoComanda.CONFIRMADA);
    }

    public ComandaDTO preparar(Integer id) {
        return cambiarEstado(id, EstadoComanda.PREPARACION);
    }

    public ComandaDTO servir(Integer id) {
        return cambiarEstado(id, EstadoComanda.SERVIDA);
    }

    public ComandaDTO cancelar(Integer id) {
        return cambiarEstado(id, EstadoComanda.CANCELADA);
    }

    private ComandaDTO cambiarEstado(Integer id, EstadoComanda nuevoEstado) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + id + " no encontrada"));
        validateTransicionComanda(comanda.getEstado(), nuevoEstado);
        comanda.setEstado(nuevoEstado);
        return new ComandaDTO(comandaRepository.save(comanda));
    }

    @Transactional(readOnly = true)
    public Page<ComandaDTO> filter(Integer idSesion, String estado, Pageable pageable) {
        return comandaRepository.filter(idSesion, estado, pageable)
                .map(ComandaDTO::new);
    }

    @Transactional(readOnly = true)
    public List<ComandaDTO> getMisComandasBySesion(String emailCliente) {
        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        List<SesionesMesa> activas = sesionesMesaRepository.findByIdClienteIdAndEstado(
                cliente.getId(), EstadoSesion.ACTIVA);
        if (activas.isEmpty()) return List.of();
        return comandaRepository.findByIdSesionId(activas.get(0).getId()).stream()
                .map(ComandaDTO::new)
                .toList();
    }

    public ComandaDTO cancelarByCliente(Integer id, String emailCliente) {
        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + id + " no encontrada"));
        SesionesMesa sesion = comanda.getIdSesion();
        if (sesion.getIdCliente() == null || !sesion.getIdCliente().getId().equals(cliente.getId())) {
            throw new IllegalStateException("No tienes permiso para cancelar esta comanda");
        }
        if (comanda.getEstado() != EstadoComanda.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden cancelar comandas pendientes");
        }
        comanda.setEstado(EstadoComanda.CANCELADA);
        return new ComandaDTO(comandaRepository.save(comanda));
    }

    public void delete(Integer id) {
        if (!comandaRepository.existsById(id)) {
            throw new EntityNotFoundException("Comanda con id " + id + " no encontrada");
        }
        comandaRepository.deleteById(id);
    }
}