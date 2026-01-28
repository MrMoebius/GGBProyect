package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.JuegoRepository;
import org.springframework.stereotype.Service;

@Service
public class JuegoService {

    private final JuegoRepository juegoRepository;

    public JuegoService(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }
}