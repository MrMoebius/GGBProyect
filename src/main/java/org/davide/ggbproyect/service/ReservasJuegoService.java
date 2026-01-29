package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.JuegosCopia;
import org.davide.ggbproyect.models.ReservasJuego;
import org.davide.ggbproyect.models.ReservasJuegoDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.repository.ReservasJuegoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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

    public Optional<ReservasJuegoDTO> getById(Integer id) {
        return reservasJuegoRepository.findById(id)
                .map(ReservasJuegoDTO::new);
    }

    public ReservasJuegoDTO create(ReservasJuegoDTO reservasJuegoDTO) {
        ReservasJuego reservasJuego = reservasJuegoDTO.toEntity();
        return new ReservasJuegoDTO(reservasJuegoRepository.save(reservasJuego));
    }

    public Optional<ReservasJuegoDTO> update(Integer id, ReservasJuegoDTO reservasJuegoDTO) {
        return reservasJuegoRepository.findById(id).map(existingReserva -> {
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
        });
    }

    public boolean delete(Integer id) {
        if (reservasJuegoRepository.existsById(id)) {
            reservasJuegoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}