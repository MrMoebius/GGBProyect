package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesEmpleadoDTO {
    private Integer id;

    @Size(max = 50)
    @NotNull
    private String nombreRol;

    public RolesEmpleadoDTO(RolesEmpleado entity) {
        this.id = entity.getId();
        this.nombreRol = entity.getNombreRol();
    }

    public RolesEmpleado toEntity() {
        RolesEmpleado entity = new RolesEmpleado();
        entity.setId(this.id);
        entity.setNombreRol(this.nombreRol);
        return entity;
    }
}