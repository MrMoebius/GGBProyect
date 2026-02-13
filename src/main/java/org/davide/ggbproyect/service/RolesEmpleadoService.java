package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.RolesEmpleado;
import org.davide.ggbproyect.models.RolesEmpleadoDTO;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.davide.ggbproyect.repository.RolesEmpleadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RolesEmpleadoService {

    private final RolesEmpleadoRepository rolesEmpleadoRepository;
    private final EmpleadoRepository empleadoRepository;

    public RolesEmpleadoService(RolesEmpleadoRepository rolesEmpleadoRepository, EmpleadoRepository empleadoRepository) {
        this.rolesEmpleadoRepository = rolesEmpleadoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Transactional(readOnly = true)
    public Page<RolesEmpleadoDTO> getAll(Pageable pageable) {
        return rolesEmpleadoRepository.findAll(pageable)
                .map(RolesEmpleadoDTO::new);
    }

    @Transactional(readOnly = true)
    public RolesEmpleadoDTO getById(Integer id) {
        return rolesEmpleadoRepository.findById(id)
                .map(RolesEmpleadoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Rol de empleado con id " + id + " no encontrado"));
    }

    public RolesEmpleadoDTO create(RolesEmpleadoDTO rolesEmpleadoDTO) {
        RolesEmpleado rolesEmpleado = rolesEmpleadoDTO.toEntity();
        List<RolesEmpleado> rolesEmpleados = rolesEmpleadoRepository.findAll();

        for (RolesEmpleado rolesEmpleado1 : rolesEmpleados) {
            if (rolesEmpleado1.getNombreRol().equals(rolesEmpleadoDTO.getNombreRol())) {
                throw new IllegalArgumentException("El nombre del rol esta duplicado ");
            }
        }
        return new RolesEmpleadoDTO(rolesEmpleadoRepository.save(rolesEmpleado));
    }

    public RolesEmpleadoDTO update(Integer id, RolesEmpleadoDTO rolesEmpleadoDTO) {
        RolesEmpleado existingRol = rolesEmpleadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol de empleado con id " + id + " no encontrado"));
        existingRol.setNombreRol(rolesEmpleadoDTO.getNombreRol());

        List<RolesEmpleado> rolesEmpleados = rolesEmpleadoRepository.findAll();

        for (RolesEmpleado rolesEmpleado1 : rolesEmpleados) {
            if (rolesEmpleado1.getNombreRol().equals(rolesEmpleadoDTO.getNombreRol()) && !rolesEmpleado1.getId().equals(id)) {
                throw new IllegalArgumentException("El nombre del rol esta duplicado ");
            }
        }

        return new RolesEmpleadoDTO(rolesEmpleadoRepository.save(existingRol));
    }

    @Transactional(readOnly = true)
    public Page<RolesEmpleadoDTO> filter(String nombreRol, Pageable pageable) {
        return rolesEmpleadoRepository.filter(nombreRol, pageable)
                .map(RolesEmpleadoDTO::new);
    }

    public void delete(Integer id) {
        if (!rolesEmpleadoRepository.existsById(id)) {
            throw new EntityNotFoundException("Rol de empleado con id " + id + " no encontrado");
        }
        if (empleadoRepository.existsByIdRol_Id(id)) {
            throw new IllegalStateException("No se puede eliminar el rol porque hay empleados asignados a el.");
        }
        rolesEmpleadoRepository.deleteById(id);
    }
}