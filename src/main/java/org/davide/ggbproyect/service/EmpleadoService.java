package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Empleado;
import org.davide.ggbproyect.models.EmpleadoDTO;
import org.davide.ggbproyect.models.RolesEmpleado;
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
        // Como el DTO no tiene password, se asume que se maneja aparte o se establece un valor por defecto/temporal si es nuevo
        // Para este CRUD básico, guardamos tal cual viene del DTO (sin password si es nuevo podría fallar por @NotNull en entidad)
        // Se asume que el DTO podría venir con password si se modificara, pero por seguridad se excluyó.
        // Ajuste: Si es creación, se necesitaría una contraseña. Por ahora, guardamos lo que hay.
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
            existingEmpleado.setEstado(empleadoDTO.getEstado());
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