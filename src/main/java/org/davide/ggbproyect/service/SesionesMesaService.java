package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.*;
import org.davide.ggbproyect.models.enums.EstadoComanda;
import org.davide.ggbproyect.models.enums.EstadoMesa;
import org.davide.ggbproyect.models.enums.EstadoPago;
import org.davide.ggbproyect.models.enums.EstadoReserva;
import org.davide.ggbproyect.models.enums.EstadoSesion;
import org.davide.ggbproyect.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class SesionesMesaService {

    private final SesionesMesaRepository sesionesMesaRepository;
    private final MesaRepository mesaRepository;
    private final ReservasMesaRepository reservasMesaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ClienteRepository clienteRepository;
    private final ComandaRepository comandaRepository;
    private final PagosMesaRepository pagosMesaRepository;
    private final LudotecaSesionesRepository ludotecaSesionesRepository;
    private final FacturaService facturaService;

    private static final Map<EstadoSesion, Set<EstadoSesion>> TRANSICIONES_SESION = Map.of(
        EstadoSesion.ACTIVA, Set.of(EstadoSesion.CERRADA, EstadoSesion.CANCELADA),
        EstadoSesion.CERRADA, Set.of(),
        EstadoSesion.CANCELADA, Set.of()
    );

    public SesionesMesaService(SesionesMesaRepository sesionesMesaRepository,
                               MesaRepository mesaRepository,
                               ReservasMesaRepository reservasMesaRepository,
                               EmpleadoRepository empleadoRepository,
                               ClienteRepository clienteRepository,
                               ComandaRepository comandaRepository,
                               PagosMesaRepository pagosMesaRepository,
                               LudotecaSesionesRepository ludotecaSesionesRepository,
                               FacturaService facturaService) {
        this.sesionesMesaRepository = sesionesMesaRepository;
        this.mesaRepository = mesaRepository;
        this.reservasMesaRepository = reservasMesaRepository;
        this.empleadoRepository = empleadoRepository;
        this.clienteRepository = clienteRepository;
        this.comandaRepository = comandaRepository;
        this.pagosMesaRepository = pagosMesaRepository;
        this.ludotecaSesionesRepository = ludotecaSesionesRepository;
        this.facturaService = facturaService;
    }

    @Transactional(readOnly = true)
    public Page<SesionesMesaDTO> getAll(Pageable pageable) {
        return sesionesMesaRepository.findAll(pageable)
                .map(SesionesMesaDTO::new);
    }

    @Transactional(readOnly = true)
    public SesionesMesaDTO getById(Integer id) {
        return sesionesMesaRepository.findById(id)
                .map(SesionesMesaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + id + " no encontrada"));
    }

    public SesionesMesaDTO create(SesionesMesaDTO sesionesMesaDTO) {
        SesionesMesa sesionesMesa = sesionesMesaDTO.toEntity();
        if (sesionesMesaDTO.getIdMesa() != null) {
            Mesa mesa = mesaRepository.findById(sesionesMesaDTO.getIdMesa())
                    .orElseThrow(() -> new EntityNotFoundException("Mesa con id " + sesionesMesaDTO.getIdMesa() + " no encontrada"));
            sesionesMesa.setIdMesa(mesa);
        }
        if (sesionesMesaDTO.getIdReserva() != null) {
            ReservasMesa reserva = reservasMesaRepository.findById(sesionesMesaDTO.getIdReserva())
                    .orElseThrow(() -> new EntityNotFoundException("Reserva de mesa con id " + sesionesMesaDTO.getIdReserva() + " no encontrada"));
            sesionesMesa.setIdReserva(reserva);
        }
        if (sesionesMesaDTO.getIdEmpleadoApertura() != null) {
            Empleado empleado = empleadoRepository.findById(sesionesMesaDTO.getIdEmpleadoApertura())
                    .orElseThrow(() -> new EntityNotFoundException("Empleado con id " + sesionesMesaDTO.getIdEmpleadoApertura() + " no encontrado"));
            sesionesMesa.setIdEmpleadoApertura(empleado);
        }
        if (sesionesMesaDTO.getIdCliente() != null) {
            Cliente cliente = clienteRepository.findById(sesionesMesaDTO.getIdCliente())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente con id " + sesionesMesaDTO.getIdCliente() + " no encontrado"));
            sesionesMesa.setIdCliente(cliente);
        }
        sesionesMesa.setNumComensales(sesionesMesaDTO.getNumComensales());
        sesionesMesa.setUsaLudoteca(sesionesMesaDTO.getUsaLudoteca());
        return new SesionesMesaDTO(sesionesMesaRepository.save(sesionesMesa));
    }

    public SesionesMesaDTO update(Integer id, SesionesMesaDTO sesionesMesaDTO) {
        SesionesMesa existingSesion = sesionesMesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + id + " no encontrada"));
        if (sesionesMesaDTO.getIdMesa() != null) {
            Mesa mesa = mesaRepository.findById(sesionesMesaDTO.getIdMesa())
                    .orElseThrow(() -> new EntityNotFoundException("Mesa con id " + sesionesMesaDTO.getIdMesa() + " no encontrada"));
            existingSesion.setIdMesa(mesa);
        }
        if (sesionesMesaDTO.getIdReserva() != null) {
            ReservasMesa reserva = reservasMesaRepository.findById(sesionesMesaDTO.getIdReserva())
                    .orElseThrow(() -> new EntityNotFoundException("Reserva de mesa con id " + sesionesMesaDTO.getIdReserva() + " no encontrada"));
            existingSesion.setIdReserva(reserva);
        }
        if (sesionesMesaDTO.getIdEmpleadoApertura() != null) {
            Empleado empleado = empleadoRepository.findById(sesionesMesaDTO.getIdEmpleadoApertura())
                    .orElseThrow(() -> new EntityNotFoundException("Empleado con id " + sesionesMesaDTO.getIdEmpleadoApertura() + " no encontrado"));
            existingSesion.setIdEmpleadoApertura(empleado);
        }
        if (sesionesMesaDTO.getIdCliente() != null) {
            Cliente cliente = clienteRepository.findById(sesionesMesaDTO.getIdCliente())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente con id " + sesionesMesaDTO.getIdCliente() + " no encontrado"));
            existingSesion.setIdCliente(cliente);
        }
        existingSesion.setNumComensales(sesionesMesaDTO.getNumComensales());
        existingSesion.setUsaLudoteca(sesionesMesaDTO.getUsaLudoteca());
        existingSesion.setInicio(sesionesMesaDTO.getInicio());
        existingSesion.setFin(sesionesMesaDTO.getFin());
        if (sesionesMesaDTO.getEstado() != null) {
            try {
                EstadoSesion nuevoEstado = EstadoSesion.valueOf(sesionesMesaDTO.getEstado());
                validateTransicionSesion(existingSesion.getEstado(), nuevoEstado);
                existingSesion.setEstado(nuevoEstado);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        return new SesionesMesaDTO(sesionesMesaRepository.save(existingSesion));
    }

    /**
     * Abre una sesion: valida mesa LIBRE, crea sesion ACTIVA,
     * mesa -> OCUPADA, reserva -> COMPLETADA, crea LudotecaSesiones si aplica.
     */
    public SesionesMesaDTO abrir(SesionesMesaDTO dto) {
        if (dto.getNumComensales() == null || dto.getNumComensales() <= 0) {
            throw new IllegalArgumentException("El numero de comensales debe ser mayor que 0");
        }
        Mesa mesa = mesaRepository.findById(dto.getIdMesa())
                .orElseThrow(() -> new EntityNotFoundException("Mesa con id " + dto.getIdMesa() + " no encontrada"));

        if (mesa.getEstado() != EstadoMesa.LIBRE && mesa.getEstado() != EstadoMesa.RESERVADA) {
            throw new IllegalStateException("La mesa no esta disponible. Estado actual: " + mesa.getEstado());
        }

        if (dto.getNumComensales() != null && mesa.getCapacidad() != null
                && dto.getNumComensales() > mesa.getCapacidad()) {
            throw new IllegalArgumentException(
                "Numero de comensales (" + dto.getNumComensales() + ") supera la capacidad de la mesa (" + mesa.getCapacidad() + ")");
        }

        SesionesMesa sesion = new SesionesMesa();
        sesion.setIdMesa(mesa);
        sesion.setEstado(EstadoSesion.ACTIVA);
        sesion.setFechaHoraApertura(Instant.now());
        sesion.setNumComensales(dto.getNumComensales());
        sesion.setUsaLudoteca(dto.getUsaLudoteca() != null ? dto.getUsaLudoteca() : false);

        if (dto.getIdCliente() != null) {
            Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente con id " + dto.getIdCliente() + " no encontrado"));
            sesion.setIdCliente(cliente);
        }

        if (dto.getIdEmpleadoApertura() != null) {
            Empleado empleado = empleadoRepository.findById(dto.getIdEmpleadoApertura())
                    .orElseThrow(() -> new EntityNotFoundException("Empleado con id " + dto.getIdEmpleadoApertura() + " no encontrado"));
            sesion.setIdEmpleadoApertura(empleado);
        }

        if (dto.getIdReserva() != null) {
            ReservasMesa reserva = reservasMesaRepository.findById(dto.getIdReserva())
                    .orElseThrow(() -> new EntityNotFoundException("Reserva con id " + dto.getIdReserva() + " no encontrada"));
            sesion.setIdReserva(reserva);
            reserva.setEstado(EstadoReserva.COMPLETADA);
            reservasMesaRepository.save(reserva);
        }

        mesa.setEstado(EstadoMesa.OCUPADA);
        mesaRepository.save(mesa);

        SesionesMesa saved = sesionesMesaRepository.save(sesion);

        if (Boolean.TRUE.equals(saved.getUsaLudoteca())) {
            LudotecaSesiones ludoteca = new LudotecaSesiones();
            ludoteca.setIdSesion(saved);
            ludoteca.setNumAdultos(0);
            ludoteca.setNumNinos613(0);
            ludoteca.setNumNinos05(0);
            ludoteca.setImporteTotal(BigDecimal.ZERO);
            ludoteca.setFechaCalculo(Instant.now());
            ludotecaSesionesRepository.save(ludoteca);
        }

        return new SesionesMesaDTO(saved);
    }

    /**
     * Cierra una sesion: valida comandas resueltas y pagos completos,
     * sesion -> CERRADA, comandas SERVIDAS -> PAGADAS, mesa -> LIBRE.
     */
    public SesionesMesaDTO cerrar(Integer id) {
        SesionesMesa sesion = sesionesMesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sesion con id " + id + " no encontrada"));

        if (sesion.getEstado() != EstadoSesion.ACTIVA) {
            throw new IllegalStateException("Solo se pueden cerrar sesiones activas. Estado actual: " + sesion.getEstado());
        }

        List<Comanda> comandas = comandaRepository.findByIdSesionId(id);
        for (Comanda c : comandas) {
            if (c.getEstado() != EstadoComanda.SERVIDA
                    && c.getEstado() != EstadoComanda.CANCELADA
                    && c.getEstado() != EstadoComanda.PAGADA) {
                throw new IllegalStateException(
                    "Comanda #" + c.getId() + " no esta resuelta (estado: " + c.getEstado() + "). Todas las comandas deben estar SERVIDAS, PAGADAS o CANCELADAS");
            }
        }

        BigDecimal totalSesion = comandas.stream()
                .filter(c -> c.getEstado() != EstadoComanda.CANCELADA)
                .map(c -> c.getTotal() != null ? c.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LudotecaSesiones ludoteca = ludotecaSesionesRepository.findByIdSesionId(id).orElse(null);
        if (ludoteca != null && ludoteca.getImporteTotal() != null) {
            totalSesion = totalSesion.add(ludoteca.getImporteTotal());
        }

        List<PagosMesa> pagos = pagosMesaRepository.findByIdSesionId(id);
        BigDecimal totalPagado = pagos.stream()
                .filter(p -> p.getEstado() == EstadoPago.PAGADO)
                .map(p -> p.getImporte() != null ? p.getImporte() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPagado.compareTo(totalSesion) < 0) {
            throw new IllegalStateException(
                "Pago insuficiente. Total sesion: " + totalSesion + ", total pagado: " + totalPagado);
        }

        for (Comanda c : comandas) {
            if (c.getEstado() == EstadoComanda.SERVIDA) {
                c.setEstado(EstadoComanda.PAGADA);
                comandaRepository.save(c);
            }
        }

        sesion.setEstado(EstadoSesion.CERRADA);
        sesion.setFechaHoraCierre(Instant.now());
        sesionesMesaRepository.save(sesion);

        Mesa mesa = sesion.getIdMesa();
        mesa.setEstado(EstadoMesa.LIBRE);
        mesaRepository.save(mesa);

        facturaService.generarFactura(id);

        return new SesionesMesaDTO(sesion);
    }

    private void validateTransicionSesion(EstadoSesion actual, EstadoSesion nuevo) {
        if (actual == nuevo) return;
        Set<EstadoSesion> permitidos = TRANSICIONES_SESION.get(actual);
        if (permitidos == null || !permitidos.contains(nuevo)) {
            throw new IllegalStateException(
                "Transicion de estado no permitida: " + actual + " -> " + nuevo);
        }
    }

    @Transactional(readOnly = true)
    public Page<SesionesMesaDTO> filter(Integer idMesa, String estado,
                                        Integer idReserva, Integer idEmpleadoApertura, Pageable pageable) {
        return sesionesMesaRepository.filter(idMesa, estado, idReserva, idEmpleadoApertura, pageable)
                .map(SesionesMesaDTO::new);
    }

    @Transactional(readOnly = true)
    public SesionesMesaDTO getMiSesionActiva(String emailCliente) {
        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        List<SesionesMesa> activas = sesionesMesaRepository.findByIdClienteIdAndEstado(cliente.getId(), EstadoSesion.ACTIVA);
        if (activas.isEmpty()) return null;
        return new SesionesMesaDTO(activas.get(0));
    }

    public void delete(Integer id) {
        SesionesMesa sesion = sesionesMesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + id + " no encontrada"));
        if (sesion.getEstado() == EstadoSesion.ACTIVA) {
            throw new IllegalStateException("No se puede eliminar una sesion activa. Ciérrala o cancélala primero.");
        }
        sesionesMesaRepository.deleteById(id);
    }
}