package org.davide.ggbproyect.controller;

import org.davide.ggbproyect.service.EmpleadoService;
import org.davide.ggbproyect.service.RolesEmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final RolesEmpleadoService rolesEmpleadoService;

    public EmpleadoController(EmpleadoService empleadoService,
                              RolesEmpleadoService rolesEmpleadoService) {
        this.empleadoService = empleadoService;
        this.rolesEmpleadoService = rolesEmpleadoService;
    }
}
