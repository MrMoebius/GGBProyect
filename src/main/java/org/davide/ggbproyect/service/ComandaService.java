package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.ComandaRepository;
import org.springframework.stereotype.Service;

@Service
public class ComandaService {

    private final ComandaRepository comandaRepository;

    public ComandaService(ComandaRepository comandaRepository) {
        this.comandaRepository = comandaRepository;
    }
}