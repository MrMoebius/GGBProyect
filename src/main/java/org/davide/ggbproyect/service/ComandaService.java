package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Comanda;
import org.davide.ggbproyect.models.ComandaDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.repository.ComandaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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

    public Optional<ComandaDTO> getById(Integer id) {
        return comandaRepository.findById(id)
                .map(ComandaDTO::new);
    }

    public ComandaDTO create(ComandaDTO comandaDTO) {
        Comanda comanda = comandaDTO.toEntity();
        return new ComandaDTO(comandaRepository.save(comanda));
    }

    public Optional<ComandaDTO> update(Integer id, ComandaDTO comandaDTO) {
        return comandaRepository.findById(id).map(existingComanda -> {
            if (comandaDTO.getIdSesion() != null) {
                SesionesMesa sesion = new SesionesMesa();
                sesion.setId(comandaDTO.getIdSesion());
                existingComanda.setIdSesion(sesion);
            }
            existingComanda.setFechaHora(comandaDTO.getFechaHora());
            existingComanda.setEstado(comandaDTO.getEstado());
            existingComanda.setTotal(comandaDTO.getTotal());
            return new ComandaDTO(comandaRepository.save(existingComanda));
        });
    }

    public boolean delete(Integer id) {
        if (comandaRepository.existsById(id)) {
            comandaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}