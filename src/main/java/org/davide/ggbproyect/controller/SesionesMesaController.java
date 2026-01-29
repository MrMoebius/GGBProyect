package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.service.SesionesMesaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sesiones-mesa")
public class SesionesMesaController {

    private final SesionesMesaService sesionesMesaService;

    public SesionesMesaController(SesionesMesaService sesionesMesaService) {
        this.sesionesMesaService = sesionesMesaService;
    }
}