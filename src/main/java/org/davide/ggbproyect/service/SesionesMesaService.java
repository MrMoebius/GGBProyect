package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Mesa;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.SesionesMesaDTO;
import org.davide.ggbproyect.models.enums.EstadoSesion;
import org.davide.ggbproyect.repository.SesionesMesaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public SesionesMesaDTO getById(Integer id) {
        return sesionesMesaRepository.findById(id)
                .map(SesionesMesaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + id + " no encontrada"));
    }

    public SesionesMesaDTO create(SesionesMesaDTO sesionesMesaDTO) {
        SesionesMesa sesionesMesa = sesionesMesaDTO.toEntity();
        return new SesionesMesaDTO(sesionesMesaRepository.save(sesionesMesa));
    }

    public SesionesMesaDTO update(Integer id, SesionesMesaDTO sesionesMesaDTO) {
        SesionesMesa existingSesion = sesionesMesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + id + " no encontrada"));
        if (sesionesMesaDTO.getIdMesa() != null) {
            Mesa mesa = new Mesa();
            mesa.setId(sesionesMesaDTO.getIdMesa());
            existingSesion.setIdMesa(mesa);
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

    public List<SesionesMesaDTO> filter(Integer idMesa, String estado,
                                        Integer idReserva, Integer idEmpleadoApertura) {
        return sesionesMesaRepository.filter(idMesa, estado, idReserva, idEmpleadoApertura)
                .stream()
                .map(SesionesMesaDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!sesionesMesaRepository.existsById(id)) {
            throw new EntityNotFoundException("Sesion de mesa con id " + id + " no encontrada");
        }
        sesionesMesaRepository.deleteById(id);
    }
}