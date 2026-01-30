package org.davide.ggbproyect.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.davide.ggbproyect.models.enums.EstadoEmpleado;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDTO {
    private Integer id;

    @Size(max = 100)
    @NotNull
    private String nombre;

    @Size(max = 150)
    @NotNull
    @Email
    private String email;

    @Size(max = 20)
    private String telefono;

    // Password excluded

    @NotNull
    private Integer idRol;

    private LocalDate fechaIngreso;

    @Size(max = 50)
    private String estado;

    public EmpleadoDTO(Empleado entity) {
        this.id = entity.getId();
        this.nombre = entity.getNombre();
        this.email = entity.getEmail();
        this.telefono = entity.getTelefono();
        this.idRol = entity.getIdRol() != null ? entity.getIdRol().getId() : null;
        this.fechaIngreso = entity.getFechaIngreso();
        this.estado = entity.getEstado() != null ? entity.getEstado().name() : null;
    }

    public Empleado toEntity() {
        Empleado entity = new Empleado();
        entity.setId(this.id);
        entity.setNombre(this.nombre);
        entity.setEmail(this.email);
        entity.setTelefono(this.telefono);
        
        if (this.idRol != null) {
            RolesEmpleado rol = new RolesEmpleado();
            rol.setId(this.idRol);
            entity.setIdRol(rol);
        }

        entity.setFechaIngreso(this.fechaIngreso);
        if (this.estado != null) {
            try {
                entity.setEstado(EstadoEmpleado.valueOf(this.estado));
            } catch (IllegalArgumentException e) {
                // Handle invalid enum
            }
        }
        return entity;
    }
}