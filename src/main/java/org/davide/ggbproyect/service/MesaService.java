package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.LayoutDTO;
import org.davide.ggbproyect.models.Mesa;
import org.davide.ggbproyect.models.MesaDTO;
import org.davide.ggbproyect.models.enums.EstadoMesa;
import org.davide.ggbproyect.models.enums.UbicacionJuego;
import org.davide.ggbproyect.repository.MesaRepository;

import java.util.Map;
import java.util.Set;
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

    private static final Map<EstadoMesa, Set<EstadoMesa>> TRANSICIONES_MESA = Map.of(
        EstadoMesa.LIBRE, Set.of(EstadoMesa.OCUPADA, EstadoMesa.RESERVADA, EstadoMesa.MANTENIMIENTO),
        EstadoMesa.RESERVADA, Set.of(EstadoMesa.OCUPADA, EstadoMesa.MANTENIMIENTO, EstadoMesa.LIBRE),
        EstadoMesa.OCUPADA, Set.of(EstadoMesa.LIBRE, EstadoMesa.MANTENIMIENTO),
        EstadoMesa.MANTENIMIENTO, Set.of(EstadoMesa.LIBRE)
    );

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    @Transactional(readOnly = true)
    public Page<MesaDTO> getAll(Pageable pageable) {
        return mesaRepository.findAll(pageable)
                .map(MesaDTO::new);
    }

    @Transactional(readOnly = true)
    public MesaDTO getById(Integer id) {
        return mesaRepository.findById(id)
                .map(MesaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Mesa con id " + id + " no encontrada"));
    }

    public MesaDTO create(MesaDTO mesaDTO) {
        Mesa mesa = mesaDTO.toEntity();
        List<Mesa> listMesa = mesaRepository.findAll();
        for (int i=0; i<listMesa.size(); i++) {
            if (listMesa.get(i).getNombreMesa().equals(mesa.getNombreMesa())) {
                throw new IllegalArgumentException("Mesa ya existente");
            }
            if (listMesa.get(i).getNumeroMesa().equals(mesa.getNumeroMesa())) {
                throw new IllegalArgumentException("Ya existe una mesa con el numero " + mesa.getNumeroMesa());
            }
        }

        return new MesaDTO(mesaRepository.save(mesa));
    }

    public MesaDTO update(Integer id, MesaDTO mesaDTO) {
        Mesa existingMesa = mesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mesa con id " + id + " no encontrada"));
        List<Mesa> listMesa = mesaRepository.findAll();
        for (int i = 0; i < listMesa.size(); i++) {
            if (listMesa.get(i).getNombreMesa().equals(mesaDTO.getNombreMesa()) && !listMesa.get(i).getId().equals(id)) {
                throw new IllegalArgumentException("Mesa ya existente");
            }
            if (listMesa.get(i).getNumeroMesa().equals(mesaDTO.getNumeroMesa()) && !listMesa.get(i).getId().equals(id)) {
                throw new IllegalArgumentException("Ya existe una mesa con el numero " + mesaDTO.getNumeroMesa());
            }
        }
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
                EstadoMesa nuevoEstado = EstadoMesa.valueOf(mesaDTO.getEstado());
                validateTransicionMesa(existingMesa.getEstado(), nuevoEstado);
                existingMesa.setEstado(nuevoEstado);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        existingMesa.setPosX(mesaDTO.getPosX());
        existingMesa.setPosY(mesaDTO.getPosY());
        existingMesa.setForma(mesaDTO.getForma());
        existingMesa.setRotacion(mesaDTO.getRotacion());
        return new MesaDTO(mesaRepository.save(existingMesa));
    }

    public void updateLayout(List<LayoutDTO> layouts) {
        for (LayoutDTO layout : layouts) {
            Mesa mesa = mesaRepository.findById(layout.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Mesa con id " + layout.getId() + " no encontrada"));
            mesa.setPosX(layout.getPosX());
            mesa.setPosY(layout.getPosY());
            mesa.setForma(layout.getForma());
            mesa.setRotacion(layout.getRotacion());
            mesaRepository.save(mesa);
        }
    }

    @Transactional(readOnly = true)
    public Page<MesaDTO> filter(String nombreMesa, String zona, String ubicacion,
                                String estado, Integer capacidad, Pageable pageable) {
        return mesaRepository.filter(nombreMesa, zona, ubicacion, estado, capacidad, pageable)
                .map(MesaDTO::new);
    }

    private void validateTransicionMesa(EstadoMesa actual, EstadoMesa nuevo) {
        if (actual == nuevo) return;
        Set<EstadoMesa> permitidos = TRANSICIONES_MESA.get(actual);
        if (permitidos == null || !permitidos.contains(nuevo)) {
            throw new IllegalStateException(
                "Transicion de estado no permitida: " + actual + " -> " + nuevo);
        }
    }

    public void delete(Integer id) {
        if (!mesaRepository.existsById(id)) {
            throw new EntityNotFoundException("Mesa con id " + id + " no encontrada");
        }
        mesaRepository.deleteById(id);
    }
}