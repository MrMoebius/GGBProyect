package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.JuegosCopia;
import org.davide.ggbproyect.models.ReservasJuego;
import org.davide.ggbproyect.models.ReservasJuegoDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.repository.JuegosCopiaRepository;
import org.davide.ggbproyect.repository.ReservasJuegoRepository;
import org.davide.ggbproyect.repository.SesionesMesaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservasJuegoService {

    private final ReservasJuegoRepository reservasJuegoRepository;
    private final SesionesMesaRepository sesionesMesaRepository;
    private final JuegosCopiaRepository juegosCopiaRepository;

    public ReservasJuegoService(ReservasJuegoRepository reservasJuegoRepository,
                                SesionesMesaRepository sesionesMesaRepository,
                                JuegosCopiaRepository juegosCopiaRepository) {
        this.reservasJuegoRepository = reservasJuegoRepository;
        this.sesionesMesaRepository = sesionesMesaRepository;
        this.juegosCopiaRepository = juegosCopiaRepository;
    }

    @Transactional(readOnly = true)
    public Page<ReservasJuegoDTO> getAll(Pageable pageable) {
        return reservasJuegoRepository.findAll(pageable)
                .map(ReservasJuegoDTO::new);
    }

    @Transactional(readOnly = true)
    public ReservasJuegoDTO getById(Integer id) {
        return reservasJuegoRepository.findById(id)
                .map(ReservasJuegoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Reserva de juego con id " + id + " no encontrada"));
    }

    public ReservasJuegoDTO create(ReservasJuegoDTO reservasJuegoDTO) {
        ReservasJuego reservasJuego = reservasJuegoDTO.toEntity();
        if (reservasJuegoDTO.getHoraInicio() != null && reservasJuegoDTO.getHoraInicio().isBefore(Instant.now())) {
            throw new IllegalArgumentException("No se puede crear una reserva de juego en el pasado");
        }
        if (reservasJuegoDTO.getHoraInicio() != null && reservasJuegoDTO.getHoraFin() != null
                && reservasJuegoDTO.getHoraInicio().isAfter(reservasJuegoDTO.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio no puede ser despues que la de fin");
        }
        if (reservasJuegoDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(reservasJuegoDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + reservasJuegoDTO.getIdSesion() + " no encontrada"));
            reservasJuego.setIdSesion(sesion);
        }
        if (reservasJuegoDTO.getIdCopia() != null) {
            JuegosCopia copia = juegosCopiaRepository.findById(reservasJuegoDTO.getIdCopia())
                    .orElseThrow(() -> new EntityNotFoundException("Copia de juego con id " + reservasJuegoDTO.getIdCopia() + " no encontrada"));
            reservasJuego.setIdCopia(copia);
        }
        return new ReservasJuegoDTO(reservasJuegoRepository.save(reservasJuego));
    }

    public ReservasJuegoDTO update(Integer id, ReservasJuegoDTO reservasJuegoDTO) {
        ReservasJuego existingReserva = reservasJuegoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva de juego con id " + id + " no encontrada"));
        if (reservasJuegoDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(reservasJuegoDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + reservasJuegoDTO.getIdSesion() + " no encontrada"));
            existingReserva.setIdSesion(sesion);
        }
        if (reservasJuegoDTO.getIdCopia() != null) {
            JuegosCopia copia = juegosCopiaRepository.findById(reservasJuegoDTO.getIdCopia())
                    .orElseThrow(() -> new EntityNotFoundException("Copia de juego con id " + reservasJuegoDTO.getIdCopia() + " no encontrada"));
            existingReserva.setIdCopia(copia);
        }
        if (reservasJuegoDTO.getHoraInicio() != null && reservasJuegoDTO.getHoraInicio().isBefore(Instant.now())) {
            throw new IllegalArgumentException("No se puede mover una reserva de juego al pasado");
        }
        if (reservasJuegoDTO.getHoraInicio() != null && reservasJuegoDTO.getHoraFin() != null
                && reservasJuegoDTO.getHoraInicio().isAfter(reservasJuegoDTO.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio no puede ser despues que la de fin");
        }
        existingReserva.setHoraInicio(reservasJuegoDTO.getHoraInicio());
        existingReserva.setHoraFin(reservasJuegoDTO.getHoraFin());
        existingReserva.setEstado(reservasJuegoDTO.getEstado());
        return new ReservasJuegoDTO(reservasJuegoRepository.save(existingReserva));
    }

    @Transactional(readOnly = true)
    public Page<ReservasJuegoDTO> filter(Integer idSesion, Integer idCopia, String estado, Pageable pageable) {
        return reservasJuegoRepository.filter(idSesion, idCopia, estado, pageable)
                .map(ReservasJuegoDTO::new);
    }

    public void delete(Integer id) {
        if (!reservasJuegoRepository.existsById(id)) {
            throw new EntityNotFoundException("Reserva de juego con id " + id + " no encontrada");
        }
        reservasJuegoRepository.deleteById(id);
    }
}