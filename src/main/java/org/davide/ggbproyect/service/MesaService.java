package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Mesa;
import org.davide.ggbproyect.models.MesaDTO;
import org.davide.ggbproyect.models.enums.EstadoMesa;
import org.davide.ggbproyect.models.enums.UbicacionJuego;
import org.davide.ggbproyect.repository.MesaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MesaService {

    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    public Page<MesaDTO> getAll(Pageable pageable) {
        return mesaRepository.findAll(pageable)
                .map(MesaDTO::new);
    }

    public MesaDTO getById(Integer id) {
        return mesaRepository.findById(id)
                .map(MesaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Mesa con id " + id + " no encontrada"));
    }

    public MesaDTO create(MesaDTO mesaDTO) {
        Mesa mesa = mesaDTO.toEntity();
        return new MesaDTO(mesaRepository.save(mesa));
    }

    public MesaDTO update(Integer id, MesaDTO mesaDTO) {
        Mesa existingMesa = mesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mesa con id " + id + " no encontrada"));
        existingMesa.setNumeroMesa(mesaDTO.getNumeroMesa());
        existingMesa.setNombreMesa(mesaDTO.getNombreMesa());
        existingMesa.setCapacidad(mesaDTO.getCapacidad());
        existingMesa.setZona(mesaDTO.getZona());
        if (mesaDTO.getUbicacion() != null) {
            try {
                existingMesa.setUbicacion(UbicacionJuego.valueOf(mesaDTO.getUbicacion()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de ubicacion invalido: " + mesaDTO.getUbicacion());
            }
        }
        if (mesaDTO.getEstado() != null) {
            try {
                existingMesa.setEstado(EstadoMesa.valueOf(mesaDTO.getEstado()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de estado invalido: " + mesaDTO.getEstado());
            }
        }
        return new MesaDTO(mesaRepository.save(existingMesa));
    }

    public List<MesaDTO> filter(String nombreMesa, String zona, String ubicacion,
                                String estado, Integer capacidad) {
        return mesaRepository.filter(nombreMesa, zona, ubicacion, estado, capacidad)
                .stream()
                .map(MesaDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!mesaRepository.existsById(id)) {
            throw new EntityNotFoundException("Mesa con id " + id + " no encontrada");
        }
        mesaRepository.deleteById(id);
    }
}