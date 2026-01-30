package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Empleado;
import org.davide.ggbproyect.models.EmpleadoDTO;
import org.davide.ggbproyect.models.RolesEmpleado;
import org.davide.ggbproyect.models.enums.EstadoEmpleado;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.davide.ggbproyect.repository.RolesEmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final RolesEmpleadoRepository rolesEmpleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository,
                           RolesEmpleadoRepository rolesEmpleadoRepository) {
        this.empleadoRepository = empleadoRepository;
        this.rolesEmpleadoRepository = rolesEmpleadoRepository;
    }

    public List<EmpleadoDTO> getAll() {
        return empleadoRepository.findAll().stream()
                .map(EmpleadoDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<EmpleadoDTO> getById(Integer id) {
        return empleadoRepository.findById(id)
                .map(EmpleadoDTO::new);
    }

    public EmpleadoDTO create(EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoDTO.toEntity();
        // Nota: La contraseña debería ser manejada/encriptada aquí o en otro lugar antes de guardar
        return new EmpleadoDTO(empleadoRepository.save(empleado));
    }

    public Optional<EmpleadoDTO> update(Integer id, EmpleadoDTO empleadoDTO) {
        return empleadoRepository.findById(id).map(existingEmpleado -> {
            existingEmpleado.setNombre(empleadoDTO.getNombre());
            existingEmpleado.setEmail(empleadoDTO.getEmail());
            existingEmpleado.setTelefono(empleadoDTO.getTelefono());
            if (empleadoDTO.getIdRol() != null) {
                RolesEmpleado rol = new RolesEmpleado();
                rol.setId(empleadoDTO.getIdRol());
                existingEmpleado.setIdRol(rol);
            }
            existingEmpleado.setFechaIngreso(empleadoDTO.getFechaIngreso());
            if (empleadoDTO.getEstado() != null) {
                try {
                    existingEmpleado.setEstado(EstadoEmpleado.valueOf(empleadoDTO.getEstado()));
                } catch (IllegalArgumentException e) {
                    // Handle invalid enum
                }
            }
            return new EmpleadoDTO(empleadoRepository.save(existingEmpleado));
        });
    }

    public boolean delete(Integer id) {
        if (empleadoRepository.existsById(id)) {
            empleadoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}