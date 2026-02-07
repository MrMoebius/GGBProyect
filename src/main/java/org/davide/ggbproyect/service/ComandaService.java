package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Comanda;
import org.davide.ggbproyect.models.ComandaDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.enums.EstadoComanda;
import org.davide.ggbproyect.repository.ComandaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComandaService {

    private final ComandaRepository comandaRepository;

    public ComandaService(ComandaRepository comandaRepository) {
        this.comandaRepository = comandaRepository;
    }

    public List<ComandaDTO> getAll() {
        return comandaRepository.findAll().stream()
                .map(ComandaDTO::new)
                .collect(Collectors.toList());
    }

    public ComandaDTO getById(Integer id) {
        return comandaRepository.findById(id)
                .map(ComandaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + id + " no encontrada"));
    }

    public ComandaDTO create(ComandaDTO comandaDTO) {
        Comanda comanda = comandaDTO.toEntity();
        return new ComandaDTO(comandaRepository.save(comanda));
    }

    public ComandaDTO update(Integer id, ComandaDTO comandaDTO) {
        Comanda existingComanda = comandaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + id + " no encontrada"));
        if (comandaDTO.getIdSesion() != null) {
            SesionesMesa sesion = new SesionesMesa();
            sesion.setId(comandaDTO.getIdSesion());
            existingComanda.setIdSesion(sesion);
        }
        existingComanda.setFechaHora(comandaDTO.getFechaHora());
        if (comandaDTO.getEstado() != null) {
            try {
                existingComanda.setEstado(EstadoComanda.valueOf(comandaDTO.getEstado()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de estado invalido: " + comandaDTO.getEstado());
            }
        }
        existingComanda.setTotal(comandaDTO.getTotal());
        return new ComandaDTO(comandaRepository.save(existingComanda));
    }

    public List<ComandaDTO> filter(Integer idSesion, String estado) {
        return comandaRepository.filter(idSesion, estado)
                .stream()
                .map(ComandaDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!comandaRepository.existsById(id)) {
            throw new EntityNotFoundException("Comanda con id " + id + " no encontrada");
        }
        comandaRepository.deleteById(id);
    }
}