package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.service.ReservasJuegoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas-juego")
public class ReservasJuegoController {

    private final ReservasJuegoService reservasJuegoService;

    public ReservasJuegoController(ReservasJuegoService reservasJuegoService) {
        this.reservasJuegoService = reservasJuegoService;
    }
}