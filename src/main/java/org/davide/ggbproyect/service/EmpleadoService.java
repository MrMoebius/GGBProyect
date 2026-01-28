package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.davide.ggbproyect.repository.RolesEmpleadoRepository;

public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final RolesEmpleadoRepository rolesEmpleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository,
                           RolesEmpleadoRepository rolesEmpleadoRepository) {
        this.empleadoRepository = empleadoRepository;
        this.rolesEmpleadoRepository = rolesEmpleadoRepository;
    }

}
