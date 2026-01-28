package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.LineasComandaRepository;
import org.springframework.stereotype.Service;

@Service
public class LineasComandaService {

    private final LineasComandaRepository lineasComandaRepository;

    public LineasComandaService(LineasComandaRepository lineasComandaRepository) {
        this.lineasComandaRepository = lineasComandaRepository;
    }
}