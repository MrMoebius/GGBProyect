package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.service.TarifasLudotecaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tarifas-ludoteca")
public class TarifasLudotecaController {

    private final TarifasLudotecaService tarifasLudotecaService;

    public TarifasLudotecaController(TarifasLudotecaService tarifasLudotecaService) {
        this.tarifasLudotecaService = tarifasLudotecaService;
    }
}