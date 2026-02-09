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

    public Page<RolesEmpleadoDTO> getAll(Pageable pageable) {
        return rolesEmpleadoRepository.findAll(pageable)
                .map(RolesEmpleadoDTO::new);
    }

    public RolesEmpleadoDTO getById(Integer id) {
        return rolesEmpleadoRepository.findById(id)
                .map(RolesEmpleadoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Rol de empleado con id " + id + " no encontrado"));
    }

    public RolesEmpleadoDTO create(RolesEmpleadoDTO rolesEmpleadoDTO) {
        RolesEmpleado rolesEmpleado = rolesEmpleadoDTO.toEntity();
        return new RolesEmpleadoDTO(rolesEmpleadoRepository.save(rolesEmpleado));
    }

    public RolesEmpleadoDTO update(Integer id, RolesEmpleadoDTO rolesEmpleadoDTO) {
        RolesEmpleado existingRol = rolesEmpleadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol de empleado con id " + id + " no encontrado"));
        existingRol.setNombreRol(rolesEmpleadoDTO.getNombreRol());
        return new RolesEmpleadoDTO(rolesEmpleadoRepository.save(existingRol));
    }

    public List<RolesEmpleadoDTO> filter(String nombreRol) {
        return rolesEmpleadoRepository.filter(nombreRol)
                .stream()
                .map(RolesEmpleadoDTO::new)
                .collect(Collectors.toList());
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