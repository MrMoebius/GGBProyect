package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.SesionesMesaRepository;
import org.springframework.stereotype.Service;

@Service
public class SesionesMesaService {

    private final SesionesMesaRepository sesionesMesaRepository;

    public SesionesMesaService(SesionesMesaRepository sesionesMesaRepository) {
        this.sesionesMesaRepository = sesionesMesaRepository;
    }
}