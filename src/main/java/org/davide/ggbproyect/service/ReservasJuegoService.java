package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.ReservasJuegoRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservasJuegoService {

    private final ReservasJuegoRepository reservasJuegoRepository;

    public ReservasJuegoService(ReservasJuegoRepository reservasJuegoRepository) {
        this.reservasJuegoRepository = reservasJuegoRepository;
    }
}