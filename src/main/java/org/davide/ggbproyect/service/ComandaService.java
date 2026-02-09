package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Comanda;
import org.davide.ggbproyect.models.ComandaDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.enums.EstadoComanda;
import org.davide.ggbproyect.repository.ComandaRepository;
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
public class ComandaService {

    private final ComandaRepository comandaRepository;
    private final SesionesMesaRepository sesionesMesaRepository;

    public ComandaService(ComandaRepository comandaRepository,
                          SesionesMesaRepository sesionesMesaRepository) {
        this.comandaRepository = comandaRepository;
        this.sesionesMesaRepository = sesionesMesaRepository;
    }

    public Page<ComandaDTO> getAll(Pageable pageable) {
        return comandaRepository.findAll(pageable)
                .map(ComandaDTO::new);
    }

    public ComandaDTO getById(Integer id) {
        return comandaRepository.findById(id)
                .map(ComandaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + id + " no encontrada"));
    }

    public ComandaDTO create(ComandaDTO comandaDTO) {
        Comanda comanda = comandaDTO.toEntity();
        if (comandaDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(comandaDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + comandaDTO.getIdSesion() + " no encontrada"));
            comanda.setIdSesion(sesion);
        }
        return new ComandaDTO(comandaRepository.save(comanda));
    }

    public ComandaDTO update(Integer id, ComandaDTO comandaDTO) {
        Comanda existingComanda = comandaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + id + " no encontrada"));
        if (comandaDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(comandaDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + comandaDTO.getIdSesion() + " no encontrada"));
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