package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.service.LudotecaSesionesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ludoteca-sesiones")
public class LudotecaSesionesController {

    private final LudotecaSesionesService ludotecaSesionesService;

    public LudotecaSesionesController(LudotecaSesionesService ludotecaSesionesService) {
        this.ludotecaSesionesService = ludotecaSesionesService;
    }
}