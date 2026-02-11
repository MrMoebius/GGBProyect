package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.PeticionesPago;
import org.davide.ggbproyect.models.PeticionesPagoDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.enums.MetodoPago;
import org.davide.ggbproyect.repository.PeticionesPagoRepository;
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
public class PeticionesPagoService {

    private final PeticionesPagoRepository peticionesPagoRepository;
    private final SesionesMesaRepository sesionesMesaRepository;

    public PeticionesPagoService(PeticionesPagoRepository peticionesPagoRepository,
                                 SesionesMesaRepository sesionesMesaRepository) {
        this.peticionesPagoRepository = peticionesPagoRepository;
        this.sesionesMesaRepository = sesionesMesaRepository;
    }

    @Transactional(readOnly = true)
    public Page<PeticionesPagoDTO> getAll(Pageable pageable) {
        return peticionesPagoRepository.findAll(pageable)
                .map(PeticionesPagoDTO::new);
    }

    @Transactional(readOnly = true)
    public PeticionesPagoDTO getById(Integer id) {
        return peticionesPagoRepository.findById(id)
                .map(PeticionesPagoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Peticion de pago con id " + id + " no encontrada"));
    }

    public PeticionesPagoDTO create(PeticionesPagoDTO peticionesPagoDTO) {
        PeticionesPago peticionesPago = peticionesPagoDTO.toEntity();
        if (peticionesPagoDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(peticionesPagoDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + peticionesPagoDTO.getIdSesion() + " no encontrada"));
            peticionesPago.setIdSesion(sesion);
        }
        return new PeticionesPagoDTO(peticionesPagoRepository.save(peticionesPago));
    }

    public PeticionesPagoDTO update(Integer id, PeticionesPagoDTO peticionesPagoDTO) {
        PeticionesPago existingPeticion = peticionesPagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Peticion de pago con id " + id + " no encontrada"));
        if (peticionesPagoDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(peticionesPagoDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + peticionesPagoDTO.getIdSesion() + " no encontrada"));
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

    @Transactional(readOnly = true)
    public Page<PeticionesPagoDTO> filter(Integer idSesion, String metodoPreferido, Boolean atendida, Pageable pageable) {
        return peticionesPagoRepository.filter(idSesion, metodoPreferido, atendida, pageable)
                .map(PeticionesPagoDTO::new);
    }

    public void delete(Integer id) {
        if (!peticionesPagoRepository.existsById(id)) {
            throw new EntityNotFoundException("Peticion de pago con id " + id + " no encontrada");
        }
        peticionesPagoRepository.deleteById(id);
    }
}