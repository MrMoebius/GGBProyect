package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Mesa;
import org.davide.ggbproyect.models.MesaDTO;
import org.davide.ggbproyect.repository.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    public List<MesaDTO> getAll() {
        return mesaRepository.findAll().stream()
                .map(MesaDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<MesaDTO> getById(Integer id) {
        return mesaRepository.findById(id)
                .map(MesaDTO::new);
    }

    public MesaDTO create(MesaDTO mesaDTO) {
        Mesa mesa = mesaDTO.toEntity();
        return new MesaDTO(mesaRepository.save(mesa));
    }

    public Optional<MesaDTO> update(Integer id, MesaDTO mesaDTO) {
        return mesaRepository.findById(id).map(existingMesa -> {
            existingMesa.setNumeroMesa(mesaDTO.getNumeroMesa());
            existingMesa.setNombreMesa(mesaDTO.getNombreMesa());
            existingMesa.setCapacidad(mesaDTO.getCapacidad());
            existingMesa.setZona(mesaDTO.getZona());
            existingMesa.setUbicacion(mesaDTO.getUbicacion());
            existingMesa.setEstado(mesaDTO.getEstado());
            return new MesaDTO(mesaRepository.save(existingMesa));
        });
    }

    public boolean delete(Integer id) {
        if (mesaRepository.existsById(id)) {
            mesaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}