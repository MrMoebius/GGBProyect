package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Empleado;
import org.davide.ggbproyect.models.EmpleadoDTO;
import org.davide.ggbproyect.models.RolesEmpleado;
import org.davide.ggbproyect.models.enums.EstadoEmpleado;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.davide.ggbproyect.repository.RolesEmpleadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final RolesEmpleadoRepository rolesEmpleadoRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpleadoService(EmpleadoRepository empleadoRepository,
                           RolesEmpleadoRepository rolesEmpleadoRepository,
                           PasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.rolesEmpleadoRepository = rolesEmpleadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<EmpleadoDTO> getAll(Pageable pageable) {
        return empleadoRepository.findAll(pageable)
                .map(EmpleadoDTO::new);
    }

    @Transactional(readOnly = true)
    public EmpleadoDTO getById(Integer id) {
        return empleadoRepository.findById(id)
                .map(EmpleadoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Empleado con id " + id + " no encontrado"));
    }

    public EmpleadoDTO create(EmpleadoDTO empleadoDTO) {
        if (empleadoDTO.getFechaIngreso() != null && empleadoDTO.getFechaIngreso().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser en el futuro");
        }
        empleadoRepository.findByEmail(empleadoDTO.getEmail())
                .ifPresent(e -> {
                    throw new IllegalStateException("Ya existe un empleado con el email: " + empleadoDTO.getEmail());
                });
        Empleado empleado = empleadoDTO.toEntity();
        if (empleadoDTO.getIdRol() != null) {
            RolesEmpleado rol = rolesEmpleadoRepository.findById(empleadoDTO.getIdRol())
                    .orElseThrow(() -> new EntityNotFoundException("Rol con id " + empleadoDTO.getIdRol() + " no encontrado"));
            empleado.setIdRol(rol);
        }
        if (empleadoDTO.getPassword() != null) {
            empleado.setPassword(passwordEncoder.encode(empleadoDTO.getPassword()));
        }
        return new EmpleadoDTO(empleadoRepository.save(empleado));
    }

    public EmpleadoDTO update(Integer id, EmpleadoDTO empleadoDTO) {
        if (empleadoDTO.getFechaIngreso() != null && empleadoDTO.getFechaIngreso().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser en el futuro");
        }
        Empleado existingEmpleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado con id " + id + " no encontrado"));
        empleadoRepository.findByEmail(empleadoDTO.getEmail())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(e -> {
                    throw new IllegalStateException("Ya existe un empleado con el email: " + empleadoDTO.getEmail());
                });
        existingEmpleado.setNombre(empleadoDTO.getNombre());
        existingEmpleado.setEmail(empleadoDTO.getEmail());
        existingEmpleado.setTelefono(empleadoDTO.getTelefono());
        if (empleadoDTO.getIdRol() != null) {
            RolesEmpleado rol = rolesEmpleadoRepository.findById(empleadoDTO.getIdRol())
                    .orElseThrow(() -> new EntityNotFoundException("Rol con id " + empleadoDTO.getIdRol() + " no encontrado"));
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

    @Transactional(readOnly = true)
    public Page<EmpleadoDTO> filter(String nombre, String email, Integer idRol, String estado, Pageable pageable) {
        return empleadoRepository.filter(nombre, email, idRol, estado, pageable)
                .map(EmpleadoDTO::new);
    }

    public void changePassword(Integer id, String currentPassword, String newPassword) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado con id " + id + " no encontrado"));
        if (!passwordEncoder.matches(currentPassword, empleado.getPassword())) {
            throw new IllegalArgumentException("La contrasena actual es incorrecta");
        }
        empleado.setPassword(passwordEncoder.encode(newPassword));
        empleadoRepository.save(empleado);
    }

    public void delete(Integer id) {
        if (!empleadoRepository.existsById(id)) {
            throw new EntityNotFoundException("Empleado con id " + id + " no encontrado");
        }
        empleadoRepository.deleteById(id);
    }
}
