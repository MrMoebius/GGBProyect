package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MesaDTO {
    private Integer id;

    @Size(max = 50)
    @NotNull
    private String nombreMesa;

    private Integer capacidad;

    @Size(max = 50)
    private String ubicacion;

    @Size(max = 20)
    private String estado;

    public MesaDTO(Mesa entity) {
        this.id = entity.getId();
        this.nombreMesa = entity.getNombreMesa();
        this.capacidad = entity.getCapacidad();
        this.ubicacion = entity.getUbicacion();
        this.estado = entity.getEstado();
    }

    public Mesa toEntity() {
        Mesa entity = new Mesa();
        entity.setId(this.id);
        entity.setNombreMesa(this.nombreMesa);
        entity.setCapacidad(this.capacidad);
        entity.setUbicacion(this.ubicacion);
        entity.setEstado(this.estado);
        return entity;
    }
}