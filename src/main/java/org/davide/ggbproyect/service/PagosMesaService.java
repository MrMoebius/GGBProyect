package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.*;
import org.davide.ggbproyect.models.enums.EstadoComanda;
import org.davide.ggbproyect.models.enums.EstadoPago;
import org.davide.ggbproyect.models.enums.EstadoSesion;
import org.davide.ggbproyect.models.enums.MetodoPago;
import org.davide.ggbproyect.repository.ComandaRepository;
import org.davide.ggbproyect.repository.LudotecaSesionesRepository;
import org.davide.ggbproyect.repository.PagosMesaRepository;
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

@Service
@Transactional
public class PagosMesaService {

    private final PagosMesaRepository pagosMesaRepository;
    private final SesionesMesaRepository sesionesMesaRepository;
    private final ComandaRepository comandaRepository;
    private final LudotecaSesionesRepository ludotecaSesionesRepository;

    private static final Map<EstadoPago, Set<EstadoPago>> TRANSICIONES_PAGO = Map.of(
        EstadoPago.PENDIENTE, Set.of(EstadoPago.PAGADO, EstadoPago.CANCELADO),
        EstadoPago.PAGADO, Set.of(),
        EstadoPago.CANCELADO, Set.of()
    );

    public PagosMesaService(PagosMesaRepository pagosMesaRepository,
                            SesionesMesaRepository sesionesMesaRepository,
                            ComandaRepository comandaRepository,
                            LudotecaSesionesRepository ludotecaSesionesRepository) {
        this.pagosMesaRepository = pagosMesaRepository;
        this.sesionesMesaRepository = sesionesMesaRepository;
        this.comandaRepository = comandaRepository;
        this.ludotecaSesionesRepository = ludotecaSesionesRepository;
    }

    @Transactional(readOnly = true)
    public Page<PagosMesaDTO> getAll(Pageable pageable) {
        return pagosMesaRepository.findAll(pageable)
                .map(PagosMesaDTO::new);
    }

    @Transactional(readOnly = true)
    public PagosMesaDTO getById(Integer id) {
        return pagosMesaRepository.findById(id)
                .map(PagosMesaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Pago de mesa con id " + id + " no encontrado"));
    }

    public PagosMesaDTO create(PagosMesaDTO pagosMesaDTO) {
        if (pagosMesaDTO.getImporte() != null && pagosMesaDTO.getImporte().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El importe del pago no puede ser negativo");
        }
        PagosMesa pagosMesa = pagosMesaDTO.toEntity();
        if (pagosMesaDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(pagosMesaDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + pagosMesaDTO.getIdSesion() + " no encontrada"));
            if (sesion.getEstado() != EstadoSesion.ACTIVA) {
                throw new IllegalStateException("No se pueden registrar pagos en sesiones no activas");
            }
            pagosMesa.setIdSesion(sesion);

            BigDecimal totalSesion = calcularTotalSesion(sesion.getId());
            BigDecimal totalPagado = calcularTotalPagado(sesion.getId());
            BigDecimal nuevoPago = pagosMesa.getImporte() != null ? pagosMesa.getImporte() : BigDecimal.ZERO;

            if (totalPagado.add(nuevoPago).compareTo(totalSesion) > 0) {
                throw new IllegalStateException(
                    "El pago excede el total de la sesion. Total: " + totalSesion
                    + ", ya pagado: " + totalPagado + ", nuevo pago: " + nuevoPago);
            }
        }
        return new PagosMesaDTO(pagosMesaRepository.save(pagosMesa));
    }

    public PagosMesaDTO update(Integer id, PagosMesaDTO pagosMesaDTO) {
        if (pagosMesaDTO.getImporte() != null && pagosMesaDTO.getImporte().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El importe del pago no puede ser negativo");
        }
        PagosMesa existingPago = pagosMesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago de mesa con id " + id + " no encontrado"));
        if (pagosMesaDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(pagosMesaDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + pagosMesaDTO.getIdSesion() + " no encontrada"));
            existingPago.setIdSesion(sesion);
        }
        existingPago.setFechaHora(pagosMesaDTO.getFechaHora());
        existingPago.setImporte(pagosMesaDTO.getImporte());
        if (pagosMesaDTO.getMetodoPago() != null) {
            try {
                existingPago.setMetodoPago(MetodoPago.valueOf(pagosMesaDTO.getMetodoPago()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de metodo de pago invalido: " + pagosMesaDTO.getMetodoPago());
            }
        }
        if (pagosMesaDTO.getEstado() != null) {
            try {
                EstadoPago nuevoEstado = EstadoPago.valueOf(pagosMesaDTO.getEstado());
                validateTransicionPago(existingPago.getEstado(), nuevoEstado);
                existingPago.setEstado(nuevoEstado);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        return new PagosMesaDTO(pagosMesaRepository.save(existingPago));
    }

    private BigDecimal calcularTotalSesion(Integer idSesion) {
        List<Comanda> comandas = comandaRepository.findByIdSesionId(idSesion);
        BigDecimal totalComandas = comandas.stream()
                .filter(c -> c.getEstado() != EstadoComanda.CANCELADA)
                .map(c -> c.getTotal() != null ? c.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LudotecaSesiones ludoteca = ludotecaSesionesRepository.findByIdSesionId(idSesion).orElse(null);
        if (ludoteca != null && ludoteca.getImporteTotal() != null) {
            totalComandas = totalComandas.add(ludoteca.getImporteTotal());
        }
        return totalComandas;
    }

    private BigDecimal calcularTotalPagado(Integer idSesion) {
        return pagosMesaRepository.findByIdSesionId(idSesion).stream()
                .filter(p -> p.getEstado() == EstadoPago.PAGADO)
                .map(p -> p.getImporte() != null ? p.getImporte() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateTransicionPago(EstadoPago actual, EstadoPago nuevo) {
        if (actual == nuevo) return;
        Set<EstadoPago> permitidos = TRANSICIONES_PAGO.get(actual);
        if (permitidos == null || !permitidos.contains(nuevo)) {
            throw new IllegalStateException(
                "Transicion de estado no permitida: " + actual + " -> " + nuevo);
        }
    }

    @Transactional(readOnly = true)
    public Page<PagosMesaDTO> filter(Integer idSesion, String metodoPago, String estado, Pageable pageable) {
        return pagosMesaRepository.filter(idSesion, metodoPago, estado, pageable)
                .map(PagosMesaDTO::new);
    }

    public void delete(Integer id) {
        if (!pagosMesaRepository.existsById(id)) {
            throw new EntityNotFoundException("Pago de mesa con id " + id + " no encontrado");
        }
        pagosMesaRepository.deleteById(id);
    }
}