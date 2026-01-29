package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.service.JuegosCopiaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/juegos-copia")
public class JuegosCopiaController {

    private final JuegosCopiaService juegosCopiaService;

    public JuegosCopiaController(JuegosCopiaService juegosCopiaService) {
        this.juegosCopiaService = juegosCopiaService;
    }
}