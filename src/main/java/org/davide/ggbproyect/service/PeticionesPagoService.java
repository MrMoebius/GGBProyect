package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.PeticionesPagoRepository;
import org.springframework.stereotype.Service;

@Service
public class PeticionesPagoService {

    private final PeticionesPagoRepository peticionesPagoRepository;

    public PeticionesPagoService(PeticionesPagoRepository peticionesPagoRepository) {
        this.peticionesPagoRepository = peticionesPagoRepository;
    }
}