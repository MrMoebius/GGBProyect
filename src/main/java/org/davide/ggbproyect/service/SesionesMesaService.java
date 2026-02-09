package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Empleado;
import org.davide.ggbproyect.models.Mesa;
import org.davide.ggbproyect.models.ReservasMesa;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.SesionesMesaDTO;
import org.davide.ggbproyect.models.enums.EstadoSesion;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.davide.ggbproyect.repository.MesaRepository;
import org.davide.ggbproyect.repository.ReservasMesaRepository;
import org.davide.ggbproyect.repository.SesionesMesaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SesionesMesaService {

    private final SesionesMesaRepository sesionesMesaRepository;
    private final MesaRepository mesaRepository;
    private final ReservasMesaRepository reservasMesaRepository;
    private final EmpleadoRepository empleadoRepository;

    public SesionesMesaService(SesionesMesaRepository sesionesMesaRepository,
                               MesaRepository mesaRepository,
                               ReservasMesaRepository reservasMesaRepository,
                               EmpleadoRepository empleadoRepository) {
        this.sesionesMesaRepository = sesionesMesaRepository;
        this.mesaRepository = mesaRepository;
        this.reservasMesaRepository = reservasMesaRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public Page<SesionesMesaDTO> getAll(Pageable pageable) {
        return sesionesMesaRepository.findAll(pageable)
                .map(SesionesMesaDTO::new);
    }

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
        existingSesion.setInicio(sesionesMesaDTO.getInicio());
        existingSesion.setFin(sesionesMesaDTO.getFin());
        if (sesionesMesaDTO.getEstado() != null) {
            try {
                existingSesion.setEstado(EstadoSesion.valueOf(sesionesMesaDTO.getEstado()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de estado invalido: " + sesionesMesaDTO.getEstado());
            }
        }
        return new SesionesMesaDTO(sesionesMesaRepository.save(existingSesion));
    }

    public Page<SesionesMesaDTO> filter(Integer idMesa, String estado,
                                        Integer idReserva, Integer idEmpleadoApertura, Pageable pageable) {
        return sesionesMesaRepository.filter(idMesa, estado, idReserva, idEmpleadoApertura, pageable)
                .map(SesionesMesaDTO::new);
    }

    public void delete(Integer id) {
        if (!sesionesMesaRepository.existsById(id)) {
            throw new EntityNotFoundException("Sesion de mesa con id " + id + " no encontrada");
        }
        sesionesMesaRepository.deleteById(id);
    }
}