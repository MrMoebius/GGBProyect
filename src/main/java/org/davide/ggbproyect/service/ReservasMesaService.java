package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Cliente;
import org.davide.ggbproyect.models.Juego;
import org.davide.ggbproyect.models.Mesa;
import org.davide.ggbproyect.models.ReservasMesa;
import org.davide.ggbproyect.models.ReservasMesaDTO;
import org.davide.ggbproyect.models.enums.EstadoReserva;
import org.davide.ggbproyect.repository.ReservasMesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservasMesaService {

    private final ReservasMesaRepository reservasMesaRepository;

    public ReservasMesaService(ReservasMesaRepository reservasMesaRepository) {
        this.reservasMesaRepository = reservasMesaRepository;
    }

    public List<ReservasMesaDTO> getAll() {
        return reservasMesaRepository.findAll().stream()
                .map(ReservasMesaDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ReservasMesaDTO> getById(Integer id) {
        return reservasMesaRepository.findById(id)
                .map(ReservasMesaDTO::new);
    }

    public ReservasMesaDTO create(ReservasMesaDTO reservasMesaDTO) {
        ReservasMesa reservasMesa = reservasMesaDTO.toEntity();
        return new ReservasMesaDTO(reservasMesaRepository.save(reservasMesa));
    }

    public Optional<ReservasMesaDTO> update(Integer id, ReservasMesaDTO reservasMesaDTO) {
        return reservasMesaRepository.findById(id).map(existingReserva -> {
            if (reservasMesaDTO.getIdCliente() != null) {
                Cliente cliente = new Cliente();
                cliente.setId(reservasMesaDTO.getIdCliente());
                existingReserva.setIdCliente(cliente);
            } else {
                existingReserva.setIdCliente(null);
            }
            if (reservasMesaDTO.getIdMesa() != null) {
                Mesa mesa = new Mesa();
                mesa.setId(reservasMesaDTO.getIdMesa());
                existingReserva.setIdMesa(mesa);
            } else {
                existingReserva.setIdMesa(null);
            }
            existingReserva.setFechaHoraInicio(reservasMesaDTO.getFechaHoraInicio());
            existingReserva.setFechaHoraFin(reservasMesaDTO.getFechaHoraFin());
            existingReserva.setNumPersonas(reservasMesaDTO.getNumPersonas());
            if (reservasMesaDTO.getIdJuegoDeseado() != null) {
                Juego juego = new Juego();
                juego.setId(reservasMesaDTO.getIdJuegoDeseado());
                existingReserva.setIdJuegoDeseado(juego);
            } else {
                existingReserva.setIdJuegoDeseado(null);
            }
            if (reservasMesaDTO.getEstado() != null) {
                try {
                    existingReserva.setEstado(EstadoReserva.valueOf(reservasMesaDTO.getEstado()));
                } catch (IllegalArgumentException e) {
                    // Handle invalid enum
                }
            }
            existingReserva.setNotas(reservasMesaDTO.getNotas());
            return new ReservasMesaDTO(reservasMesaRepository.save(existingReserva));
        });
    }

    public boolean delete(Integer id) {
        if (reservasMesaRepository.existsById(id)) {
            reservasMesaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}