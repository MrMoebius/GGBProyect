package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.LudotecaSesionesRepository;
import org.springframework.stereotype.Service;

@Service
public class LudotecaSesionesService {

    private final LudotecaSesionesRepository ludotecaSesionesRepository;

    public LudotecaSesionesService(LudotecaSesionesRepository ludotecaSesionesRepository) {
        this.ludotecaSesionesRepository = ludotecaSesionesRepository;
    }
}