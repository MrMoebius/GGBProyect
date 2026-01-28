package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.RolesEmpleadoRepository;

public class RolesEmpleadoService {

    private final RolesEmpleadoRepository repo;

    public RolesEmpleadoService(RolesEmpleadoRepository repo) {
        this.repo = repo;
    }

}
