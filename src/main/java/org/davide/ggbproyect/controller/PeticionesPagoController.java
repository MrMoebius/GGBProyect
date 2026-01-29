package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.service.PeticionesPagoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/peticiones-pago")
public class PeticionesPagoController {

    private final PeticionesPagoService peticionesPagoService;

    public PeticionesPagoController(PeticionesPagoService peticionesPagoService) {
        this.peticionesPagoService = peticionesPagoService;
    }
}