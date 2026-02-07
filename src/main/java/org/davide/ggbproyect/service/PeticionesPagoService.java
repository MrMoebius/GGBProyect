package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.PeticionesPago;
import org.davide.ggbproyect.models.PeticionesPagoDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.enums.MetodoPago;
import org.davide.ggbproyect.repository.PeticionesPagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public PeticionesPagoDTO getById(Integer id) {
        return peticionesPagoRepository.findById(id)
                .map(PeticionesPagoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Peticion de pago con id " + id + " no encontrada"));
    }

    public PeticionesPagoDTO create(PeticionesPagoDTO peticionesPagoDTO) {
        PeticionesPago peticionesPago = peticionesPagoDTO.toEntity();
        return new PeticionesPagoDTO(peticionesPagoRepository.save(peticionesPago));
    }

    public PeticionesPagoDTO update(Integer id, PeticionesPagoDTO peticionesPagoDTO) {
        PeticionesPago existingPeticion = peticionesPagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Peticion de pago con id " + id + " no encontrada"));
        if (peticionesPagoDTO.getIdSesion() != null) {
            SesionesMesa sesion = new SesionesMesa();
            sesion.setId(peticionesPagoDTO.getIdSesion());
            existingPeticion.setIdSesion(sesion);
        }
        if (peticionesPagoDTO.getMetodoPreferido() != null) {
            try {
                existingPeticion.setMetodoPreferido(MetodoPago.valueOf(peticionesPagoDTO.getMetodoPreferido()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de metodo preferido invalido: " + peticionesPagoDTO.getMetodoPreferido());
            }
        }
        existingPeticion.setAtendida(peticionesPagoDTO.getAtendida());
        existingPeticion.setFechaPeticion(peticionesPagoDTO.getFechaPeticion());
        return new PeticionesPagoDTO(peticionesPagoRepository.save(existingPeticion));
    }

    public List<PeticionesPagoDTO> filter(Integer idSesion, String metodoPreferido, Boolean atendida) {
        return peticionesPagoRepository.filter(idSesion, metodoPreferido, atendida)
                .stream()
                .map(PeticionesPagoDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!peticionesPagoRepository.existsById(id)) {
            throw new EntityNotFoundException("Peticion de pago con id " + id + " no encontrada");
        }
        peticionesPagoRepository.deleteById(id);
    }
}