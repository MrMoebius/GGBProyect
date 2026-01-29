package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Empleado;
import org.davide.ggbproyect.models.Mesa;
import org.davide.ggbproyect.models.ReservasMesa;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.SesionesMesaDTO;
import org.davide.ggbproyect.repository.SesionesMesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SesionesMesaService {

    private final SesionesMesaRepository sesionesMesaRepository;

    public SesionesMesaService(SesionesMesaRepository sesionesMesaRepository) {
        this.sesionesMesaRepository = sesionesMesaRepository;
    }

    public List<SesionesMesaDTO> getAll() {
        return sesionesMesaRepository.findAll().stream()
                .map(SesionesMesaDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<SesionesMesaDTO> getById(Integer id) {
        return sesionesMesaRepository.findById(id)
                .map(SesionesMesaDTO::new);
    }

    public SesionesMesaDTO create(SesionesMesaDTO sesionesMesaDTO) {
        SesionesMesa sesionesMesa = sesionesMesaDTO.toEntity();
        return new SesionesMesaDTO(sesionesMesaRepository.save(sesionesMesa));
    }

    public Optional<SesionesMesaDTO> update(Integer id, SesionesMesaDTO sesionesMesaDTO) {
        return sesionesMesaRepository.findById(id).map(existingSesion -> {
            if (sesionesMesaDTO.getIdMesa() != null) {
                Mesa mesa = new Mesa();
                mesa.setId(sesionesMesaDTO.getIdMesa());
                existingSesion.setIdMesa(mesa);
            }
            if (sesionesMesaDTO.getIdReserva() != null) {
                ReservasMesa reserva = new ReservasMesa();
                reserva.setId(sesionesMesaDTO.getIdReserva());
                existingSesion.setIdReserva(reserva);
            } else {
                existingSesion.setIdReserva(null);
            }
            if (sesionesMesaDTO.getIdEmpleadoApertura() != null) {
                Empleado empleado = new Empleado();
                empleado.setId(sesionesMesaDTO.getIdEmpleadoApertura());
                existingSesion.setIdEmpleadoApertura(empleado);
            } else {
                existingSesion.setIdEmpleadoApertura(null);
            }
            existingSesion.setFechaHoraApertura(sesionesMesaDTO.getFechaHoraApertura());
            existingSesion.setFechaHoraCierre(sesionesMesaDTO.getFechaHoraCierre());
            existingSesion.setEstado(sesionesMesaDTO.getEstado());
            return new SesionesMesaDTO(sesionesMesaRepository.save(existingSesion));
        });
    }

    public boolean delete(Integer id) {
        if (sesionesMesaRepository.existsById(id)) {
            sesionesMesaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}