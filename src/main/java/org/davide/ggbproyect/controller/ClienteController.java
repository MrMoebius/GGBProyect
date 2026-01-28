package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.service.ClienteService;
import org.davide.ggbproyect.service.EmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;

    public ClienteController(ClienteService clienteService,
                             EmpleadoService empleadoService) {
        this.clienteService = clienteService;
        this.empleadoService = empleadoService;
    }
}
