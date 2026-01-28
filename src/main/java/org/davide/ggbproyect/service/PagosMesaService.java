package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.PagosMesaRepository;
import org.springframework.stereotype.Service;

@Service
public class PagosMesaService {

    private final PagosMesaRepository pagosMesaRepository;

    public PagosMesaService(PagosMesaRepository pagosMesaRepository) {
        this.pagosMesaRepository = pagosMesaRepository;
    }
}