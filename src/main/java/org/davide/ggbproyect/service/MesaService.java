package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.MesaRepository;
import org.springframework.stereotype.Service;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }
}