package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.PeticionesPago;
import org.davide.ggbproyect.models.PeticionesPagoDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.enums.MetodoPago;
import org.davide.ggbproyect.repository.PeticionesPagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PeticionesPagoService {

    private final PeticionesPagoRepository peticionesPagoRepository;

    public PeticionesPagoService(PeticionesPagoRepository peticionesPagoRepository) {
        this.peticionesPagoRepository = peticionesPagoRepository;
    }

    public List<PeticionesPagoDTO> getAll() {
        return peticionesPagoRepository.findAll().stream()
                .map(PeticionesPagoDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<PeticionesPagoDTO> getById(Integer id) {
        return peticionesPagoRepository.findById(id)
                .map(PeticionesPagoDTO::new);
    }

    public PeticionesPagoDTO create(PeticionesPagoDTO peticionesPagoDTO) {
        PeticionesPago peticionesPago = peticionesPagoDTO.toEntity();
        return new PeticionesPagoDTO(peticionesPagoRepository.save(peticionesPago));
    }

    public Optional<PeticionesPagoDTO> update(Integer id, PeticionesPagoDTO peticionesPagoDTO) {
        return peticionesPagoRepository.findById(id).map(existingPeticion -> {
            if (peticionesPagoDTO.getIdSesion() != null) {
                SesionesMesa sesion = new SesionesMesa();
                sesion.setId(peticionesPagoDTO.getIdSesion());
                existingPeticion.setIdSesion(sesion);
            }
            if (peticionesPagoDTO.getMetodoPreferido() != null) {
                try {
                    existingPeticion.setMetodoPreferido(MetodoPago.valueOf(peticionesPagoDTO.getMetodoPreferido()));
                } catch (IllegalArgumentException e) {
                    // Handle invalid enum
                }
            }
            existingPeticion.setAtendida(peticionesPagoDTO.getAtendida());
            existingPeticion.setFechaPeticion(peticionesPagoDTO.getFechaPeticion());
            return new PeticionesPagoDTO(peticionesPagoRepository.save(existingPeticion));
        });
    }

    public boolean delete(Integer id) {
        if (peticionesPagoRepository.existsById(id)) {
            peticionesPagoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}