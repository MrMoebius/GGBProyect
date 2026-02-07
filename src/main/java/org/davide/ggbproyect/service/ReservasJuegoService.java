package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.JuegosCopia;
import org.davide.ggbproyect.models.ReservasJuego;
import org.davide.ggbproyect.models.ReservasJuegoDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.repository.ReservasJuegoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservasJuegoService {

    private final ReservasJuegoRepository reservasJuegoRepository;

    public ReservasJuegoService(ReservasJuegoRepository reservasJuegoRepository) {
        this.reservasJuegoRepository = reservasJuegoRepository;
    }

    public List<ReservasJuegoDTO> getAll() {
        return reservasJuegoRepository.findAll().stream()
                .map(ReservasJuegoDTO::new)
                .collect(Collectors.toList());
    }

    public ReservasJuegoDTO getById(Integer id) {
        return reservasJuegoRepository.findById(id)
                .map(ReservasJuegoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Reserva de juego con id " + id + " no encontrada"));
    }

    public ReservasJuegoDTO create(ReservasJuegoDTO reservasJuegoDTO) {
        ReservasJuego reservasJuego = reservasJuegoDTO.toEntity();
        return new ReservasJuegoDTO(reservasJuegoRepository.save(reservasJuego));
    }

    public ReservasJuegoDTO update(Integer id, ReservasJuegoDTO reservasJuegoDTO) {
        ReservasJuego existingReserva = reservasJuegoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva de juego con id " + id + " no encontrada"));
        if (reservasJuegoDTO.getIdSesion() != null) {
            SesionesMesa sesion = new SesionesMesa();
            sesion.setId(reservasJuegoDTO.getIdSesion());
            existingReserva.setIdSesion(sesion);
        }
        if (reservasJuegoDTO.getIdCopia() != null) {
            JuegosCopia copia = new JuegosCopia();
            copia.setId(reservasJuegoDTO.getIdCopia());
            existingReserva.setIdCopia(copia);
        }
        existingReserva.setHoraInicio(reservasJuegoDTO.getHoraInicio());
        existingReserva.setHoraFin(reservasJuegoDTO.getHoraFin());
        existingReserva.setEstado(reservasJuegoDTO.getEstado());
        return new ReservasJuegoDTO(reservasJuegoRepository.save(existingReserva));
    }

    public List<ReservasJuegoDTO> filter(Integer idSesion, Integer idCopia, String estado) {
        return reservasJuegoRepository.filter(idSesion, idCopia, estado)
                .stream()
                .map(ReservasJuegoDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!reservasJuegoRepository.existsById(id)) {
            throw new EntityNotFoundException("Reserva de juego con id " + id + " no encontrada");
        }
        reservasJuegoRepository.deleteById(id);
    }
}