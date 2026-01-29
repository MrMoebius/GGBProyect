package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.TarifasLudoteca;
import org.davide.ggbproyect.models.TarifasLudotecaDTO;
import org.davide.ggbproyect.repository.TarifasLudotecaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TarifasLudotecaService {

    private final TarifasLudotecaRepository tarifasLudotecaRepository;

    public TarifasLudotecaService(TarifasLudotecaRepository tarifasLudotecaRepository) {
        this.tarifasLudotecaRepository = tarifasLudotecaRepository;
    }

    public List<TarifasLudotecaDTO> getAll() {
        return tarifasLudotecaRepository.findAll().stream()
                .map(TarifasLudotecaDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<TarifasLudotecaDTO> getById(Integer id) {
        return tarifasLudotecaRepository.findById(id)
                .map(TarifasLudotecaDTO::new);
    }

    public TarifasLudotecaDTO create(TarifasLudotecaDTO tarifasLudotecaDTO) {
        TarifasLudoteca tarifasLudoteca = tarifasLudotecaDTO.toEntity();
        return new TarifasLudotecaDTO(tarifasLudotecaRepository.save(tarifasLudoteca));
    }

    public Optional<TarifasLudotecaDTO> update(Integer id, TarifasLudotecaDTO tarifasLudotecaDTO) {
        return tarifasLudotecaRepository.findById(id).map(existingTarifa -> {
            existingTarifa.setNombreTramo(tarifasLudotecaDTO.getNombreTramo());
            existingTarifa.setEdadMin(tarifasLudotecaDTO.getEdadMin());
            existingTarifa.setEdadMax(tarifasLudotecaDTO.getEdadMax());
            existingTarifa.setPrecio(tarifasLudotecaDTO.getPrecio());
            existingTarifa.setActivo(tarifasLudotecaDTO.getActivo());
            existingTarifa.setDescripcion(tarifasLudotecaDTO.getDescripcion());
            return new TarifasLudotecaDTO(tarifasLudotecaRepository.save(existingTarifa));
        });
    }

    public boolean delete(Integer id) {
        if (tarifasLudotecaRepository.existsById(id)) {
            tarifasLudotecaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}