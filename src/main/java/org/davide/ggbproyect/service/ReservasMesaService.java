package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.ReservasMesaRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservasMesaService {

    private final ReservasMesaRepository reservasMesaRepository;

    public ReservasMesaService(ReservasMesaRepository reservasMesaRepository) {
        this.reservasMesaRepository = reservasMesaRepository;
    }
}