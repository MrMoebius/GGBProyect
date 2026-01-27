package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComandaDTO {
    private Integer id;

    @NotNull
    private Integer idSesion;

    private Integer idEmpleado;

    private Instant fechaHora;

    @Size(max = 30)
    private String estado;

    private String notas;

    public ComandaDTO(Comanda entity) {
        this.id = entity.getId();
        this.idSesion = entity.getIdSesion() != null ? entity.getIdSesion().getId() : null;
        this.idEmpleado = entity.getIdEmpleado() != null ? entity.getIdEmpleado().getId() : null;
        this.fechaHora = entity.getFechaHora();
        this.estado = entity.getEstado();
        this.notas = entity.getNotas();
    }

    public Comanda toEntity() {
        Comanda entity = new Comanda();
        entity.setId(this.id);
        // Note: idSesion and idEmpleado are entities, so they need to be set separately or fetched
        // For DTO to Entity conversion, usually we set the ID if we are just passing data, 
        // but since the entity expects object references, we might need a service to fetch them.
        // However, the requirement is to return a new instance with the data from the DTO.
        // We will set the fields that are simple types. Relationships usually handled in Service layer.
        // But to strictly follow "return a new instance of the entity with the data from the DTO",
        // we can create dummy objects with IDs if needed, or leave them null if the context implies simple mapping.
        // Given the instructions, I will instantiate the related objects with just the ID if present.
        
        if (this.idSesion != null) {
            SesionesMesa sesion = new SesionesMesa();
            sesion.setId(this.idSesion);
            entity.setIdSesion(sesion);
        }
        
        if (this.idEmpleado != null) {
            Empleado empleado = new Empleado();
            empleado.setId(this.idEmpleado);
            entity.setIdEmpleado(empleado);
        }

        entity.setFechaHora(this.fechaHora);
        entity.setEstado(this.estado);
        entity.setNotas(this.notas);
        return entity;
    }
}