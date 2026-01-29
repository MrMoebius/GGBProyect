package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.RolesEmpleado;
import org.davide.ggbproyect.models.RolesEmpleadoDTO;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.davide.ggbproyect.repository.RolesEmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RolesEmpleadoService {

    private final RolesEmpleadoRepository rolesEmpleadoRepository;
    private final EmpleadoRepository empleadoRepository;

    public RolesEmpleadoService(RolesEmpleadoRepository rolesEmpleadoRepository, EmpleadoRepository empleadoRepository) {
        this.rolesEmpleadoRepository = rolesEmpleadoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public List<RolesEmpleadoDTO> getAll() {
        return rolesEmpleadoRepository.findAll().stream()
                .map(RolesEmpleadoDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<RolesEmpleadoDTO> getById(Integer id) {
        return rolesEmpleadoRepository.findById(id)
                .map(RolesEmpleadoDTO::new);
    }

    public RolesEmpleadoDTO create(RolesEmpleadoDTO rolesEmpleadoDTO) {
        RolesEmpleado rolesEmpleado = rolesEmpleadoDTO.toEntity();
        return new RolesEmpleadoDTO(rolesEmpleadoRepository.save(rolesEmpleado));
    }

    public Optional<RolesEmpleadoDTO> update(Integer id, RolesEmpleadoDTO rolesEmpleadoDTO) {
        return rolesEmpleadoRepository.findById(id).map(existingRol -> {
            existingRol.setNombreRol(rolesEmpleadoDTO.getNombreRol());
            return new RolesEmpleadoDTO(rolesEmpleadoRepository.save(existingRol));
        });
    }

    public boolean delete(Integer id) {
        if (rolesEmpleadoRepository.existsById(id)) {
            // Verificar si hay empleados asignados a este rol antes de borrar
            if (empleadoRepository.existsByIdRol_Id(id)) {
                throw new IllegalStateException("No se puede eliminar el rol porque hay empleados asignados a él.");
            }
            rolesEmpleadoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}