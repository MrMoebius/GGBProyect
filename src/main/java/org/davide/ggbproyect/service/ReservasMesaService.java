package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Cliente;
import org.davide.ggbproyect.models.Juego;
import org.davide.ggbproyect.models.Mesa;
import org.davide.ggbproyect.models.ReservasMesa;
import org.davide.ggbproyect.models.ReservasMesaDTO;
import org.davide.ggbproyect.models.enums.EstadoReserva;
import org.davide.ggbproyect.repository.ClienteRepository;
import org.davide.ggbproyect.repository.JuegoRepository;
import org.davide.ggbproyect.repository.MesaRepository;
import org.davide.ggbproyect.repository.ReservasMesaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservasMesaService {

    private final ReservasMesaRepository reservasMesaRepository;
    private final ClienteRepository clienteRepository;
    private final MesaRepository mesaRepository;
    private final JuegoRepository juegoRepository;

    public ReservasMesaService(ReservasMesaRepository reservasMesaRepository,
                               ClienteRepository clienteRepository,
                               MesaRepository mesaRepository,
                               JuegoRepository juegoRepository) {
        this.reservasMesaRepository = reservasMesaRepository;
        this.clienteRepository = clienteRepository;
        this.mesaRepository = mesaRepository;
        this.juegoRepository = juegoRepository;
    }

    public Page<ReservasMesaDTO> getAll(Pageable pageable) {
        return reservasMesaRepository.findAll(pageable)
                .map(ReservasMesaDTO::new);
    }

    public ReservasMesaDTO getById(Integer id) {
        return reservasMesaRepository.findById(id)
                .map(ReservasMesaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Reserva de mesa con id " + id + " no encontrada"));
    }

    public ReservasMesaDTO create(ReservasMesaDTO reservasMesaDTO) {
        ReservasMesa reservasMesa = reservasMesaDTO.toEntity();
        if (reservasMesaDTO.getIdCliente() != null) {
            Cliente cliente = clienteRepository.findById(reservasMesaDTO.getIdCliente())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente con id " + reservasMesaDTO.getIdCliente() + " no encontrado"));
            reservasMesa.setIdCliente(cliente);
        }
        if (reservasMesaDTO.getIdMesa() != null) {
            Mesa mesa = mesaRepository.findById(reservasMesaDTO.getIdMesa())
                    .orElseThrow(() -> new EntityNotFoundException("Mesa con id " + reservasMesaDTO.getIdMesa() + " no encontrada"));
            reservasMesa.setIdMesa(mesa);
        }
        if (reservasMesaDTO.getIdJuegoDeseado() != null) {
            Juego juego = juegoRepository.findById(reservasMesaDTO.getIdJuegoDeseado())
                    .orElseThrow(() -> new EntityNotFoundException("Juego con id " + reservasMesaDTO.getIdJuegoDeseado() + " no encontrado"));
            reservasMesa.setIdJuegoDeseado(juego);
        }
        return new ReservasMesaDTO(reservasMesaRepository.save(reservasMesa));
    }

    public ReservasMesaDTO update(Integer id, ReservasMesaDTO reservasMesaDTO) {
        ReservasMesa existingReserva = reservasMesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva de mesa con id " + id + " no encontrada"));
        if (reservasMesaDTO.getIdCliente() != null) {
            Cliente cliente = clienteRepository.findById(reservasMesaDTO.getIdCliente())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente con id " + reservasMesaDTO.getIdCliente() + " no encontrado"));
            existingReserva.setIdCliente(cliente);
        } else {
            existingReserva.setIdCliente(null);
        }
        if (reservasMesaDTO.getIdMesa() != null) {
            Mesa mesa = mesaRepository.findById(reservasMesaDTO.getIdMesa())
                    .orElseThrow(() -> new EntityNotFoundException("Mesa con id " + reservasMesaDTO.getIdMesa() + " no encontrada"));
            existingReserva.setIdMesa(mesa);
        } else {
            existingReserva.setIdMesa(null);
        }
        existingReserva.setFechaHoraInicio(reservasMesaDTO.getFechaHoraInicio());
        existingReserva.setFechaHoraFin(reservasMesaDTO.getFechaHoraFin());
        existingReserva.setNumPersonas(reservasMesaDTO.getNumPersonas());
        if (reservasMesaDTO.getIdJuegoDeseado() != null) {
            Juego juego = juegoRepository.findById(reservasMesaDTO.getIdJuegoDeseado())
                    .orElseThrow(() -> new EntityNotFoundException("Juego con id " + reservasMesaDTO.getIdJuegoDeseado() + " no encontrado"));
            existingReserva.setIdJuegoDeseado(juego);
        } else {
            existingReserva.setIdJuegoDeseado(null);
        }
        if (reservasMesaDTO.getEstado() != null) {
            try {
                existingReserva.setEstado(EstadoReserva.valueOf(reservasMesaDTO.getEstado()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de estado invalido: " + reservasMesaDTO.getEstado());
            }
        }
        existingReserva.setNotas(reservasMesaDTO.getNotas());
        return new ReservasMesaDTO(reservasMesaRepository.save(existingReserva));
    }

    public List<ReservasMesaDTO> filter(Integer idCliente, Integer idMesa,
                                        Integer idJuegoDeseado, String estado) {
        return reservasMesaRepository.filter(idCliente, idMesa, idJuegoDeseado, estado)
                .stream()
                .map(ReservasMesaDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!reservasMesaRepository.existsById(id)) {
            throw new EntityNotFoundException("Reserva de mesa con id " + id + " no encontrada");
        }
        reservasMesaRepository.deleteById(id);
    }
}