package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.TarifasLudotecaRepository;
import org.springframework.stereotype.Service;

@Service
public class TarifasLudotecaService {

    private final TarifasLudotecaRepository tarifasLudotecaRepository;

    public TarifasLudotecaService(TarifasLudotecaRepository tarifasLudotecaRepository) {
        this.tarifasLudotecaRepository = tarifasLudotecaRepository;
    }
}