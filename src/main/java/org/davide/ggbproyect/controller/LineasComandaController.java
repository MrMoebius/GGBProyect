package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.service.LineasComandaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lineas-comanda")
public class LineasComandaController {

    private final LineasComandaService lineasComandaService;

    public LineasComandaController(LineasComandaService lineasComandaService) {
        this.lineasComandaService = lineasComandaService;
    }
}