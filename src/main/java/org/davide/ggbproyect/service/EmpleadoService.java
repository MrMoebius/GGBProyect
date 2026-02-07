package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Empleado;
import org.davide.ggbproyect.models.EmpleadoDTO;
import org.davide.ggbproyect.models.RolesEmpleado;
import org.davide.ggbproyect.models.enums.EstadoEmpleado;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpleadoService(EmpleadoRepository empleadoRepository,
                           PasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<EmpleadoDTO> getAll() {
        return empleadoRepository.findAll().stream()
                .map(EmpleadoDTO::new)
                .collect(Collectors.toList());
    }

    public EmpleadoDTO getById(Integer id) {
        return empleadoRepository.findById(id)
                .map(EmpleadoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Empleado con id " + id + " no encontrado"));
    }

    public EmpleadoDTO create(EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoDTO.toEntity();
        if (empleadoDTO.getPassword() != null) {
            empleado.setPassword(passwordEncoder.encode(empleadoDTO.getPassword()));
        }
        return new EmpleadoDTO(empleadoRepository.save(empleado));
    }

    public EmpleadoDTO update(Integer id, EmpleadoDTO empleadoDTO) {
        Empleado existingEmpleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado con id " + id + " no encontrado"));
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
                throw new IllegalArgumentException("Valor de estado invalido: " + empleadoDTO.getEstado());
            }
        }
        if (empleadoDTO.getPassword() != null && !empleadoDTO.getPassword().isBlank()) {
            existingEmpleado.setPassword(passwordEncoder.encode(empleadoDTO.getPassword()));
        }
        return new EmpleadoDTO(empleadoRepository.save(existingEmpleado));
    }

    public List<EmpleadoDTO> filter(String nombre, String email, Integer idRol, String estado) {
        return empleadoRepository.filter(nombre, email, idRol, estado)
                .stream()
                .map(EmpleadoDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!empleadoRepository.existsById(id)) {
            throw new EntityNotFoundException("Empleado con id " + id + " no encontrado");
        }
        empleadoRepository.deleteById(id);
    }
}
