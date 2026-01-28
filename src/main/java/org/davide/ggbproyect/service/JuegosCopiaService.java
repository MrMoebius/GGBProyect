package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.JuegosCopiaRepository;
import org.springframework.stereotype.Service;

@Service
public class JuegosCopiaService {

    private final JuegosCopiaRepository juegosCopiaRepository;

    public JuegosCopiaService(JuegosCopiaRepository juegosCopiaRepository) {
        this.juegosCopiaRepository = juegosCopiaRepository;
    }
}