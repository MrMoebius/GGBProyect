package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.TarifasLudoteca;
import org.davide.ggbproyect.models.TarifasLudotecaDTO;
import org.davide.ggbproyect.repository.TarifasLudotecaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TarifasLudotecaService {

    private final TarifasLudotecaRepository tarifasLudotecaRepository;

    public TarifasLudotecaService(TarifasLudotecaRepository tarifasLudotecaRepository) {
        this.tarifasLudotecaRepository = tarifasLudotecaRepository;
    }

    @Transactional(readOnly = true)
    public Page<TarifasLudotecaDTO> getAll(Pageable pageable) {
        return tarifasLudotecaRepository.findAll(pageable)
                .map(TarifasLudotecaDTO::new);
    }

    @Transactional(readOnly = true)
    public TarifasLudotecaDTO getById(Integer id) {
        return tarifasLudotecaRepository.findById(id)
                .map(TarifasLudotecaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Tarifa de ludoteca con id " + id + " no encontrada"));
    }

    public TarifasLudotecaDTO create(TarifasLudotecaDTO tarifasLudotecaDTO) {
        TarifasLudoteca tarifasLudoteca = tarifasLudotecaDTO.toEntity();
        if (tarifasLudotecaDTO.getEdadMin()>tarifasLudotecaDTO.getEdadMax())
        {
            throw new IllegalArgumentException("La edad minima no puede ser mayor que la maxima ");
        }

        return new TarifasLudotecaDTO(tarifasLudotecaRepository.save(tarifasLudoteca));
    }

    public TarifasLudotecaDTO update(Integer id, TarifasLudotecaDTO tarifasLudotecaDTO) {
        TarifasLudoteca existingTarifa = tarifasLudotecaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarifa de ludoteca con id " + id + " no encontrada"));

        if (tarifasLudotecaDTO.getEdadMin() > tarifasLudotecaDTO.getEdadMax()) {
            throw new IllegalArgumentException("La edad minima no puede ser mayor que la maxima ");
        }

        existingTarifa.setNombreTramo(tarifasLudotecaDTO.getNombreTramo());
        existingTarifa.setEdadMin(tarifasLudotecaDTO.getEdadMin());
        existingTarifa.setEdadMax(tarifasLudotecaDTO.getEdadMax());
        existingTarifa.setPrecio(tarifasLudotecaDTO.getPrecio());
        existingTarifa.setActivo(tarifasLudotecaDTO.getActivo());
        existingTarifa.setDescripcion(tarifasLudotecaDTO.getDescripcion());

        return new TarifasLudotecaDTO(tarifasLudotecaRepository.save(existingTarifa));
    }

    @Transactional(readOnly = true)
    public Page<TarifasLudotecaDTO> filter(String nombreTramo, Boolean activo, Pageable pageable) {
        return tarifasLudotecaRepository.filter(nombreTramo, activo, pageable)
                .map(TarifasLudotecaDTO::new);
    }

    public void delete(Integer id) {
        if (!tarifasLudotecaRepository.existsById(id)) {
            throw new EntityNotFoundException("Tarifa de ludoteca con id " + id + " no encontrada");
        }
        tarifasLudotecaRepository.deleteById(id);
    }
}