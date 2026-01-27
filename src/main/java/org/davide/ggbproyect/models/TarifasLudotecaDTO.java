package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifasLudotecaDTO {
    private Integer id;

    @Size(max = 50)
    @NotNull
    private String nombreTramo;

    @NotNull
    private Integer edadMin;

    @NotNull
    private Integer edadMax;

    @NotNull
    private BigDecimal precio;

    private Boolean activo;

    @Size(max = 255)
    private String descripcion;

    public TarifasLudotecaDTO(TarifasLudoteca entity) {
        this.id = entity.getId();
        this.nombreTramo = entity.getNombreTramo();
        this.edadMin = entity.getEdadMin();
        this.edadMax = entity.getEdadMax();
        this.precio = entity.getPrecio();
        this.activo = entity.getActivo();
        this.descripcion = entity.getDescripcion();
    }

    public TarifasLudoteca toEntity() {
        TarifasLudoteca entity = new TarifasLudoteca();
        entity.setId(this.id);
        entity.setNombreTramo(this.nombreTramo);
        entity.setEdadMin(this.edadMin);
        entity.setEdadMax(this.edadMax);
        entity.setPrecio(this.precio);
        entity.setActivo(this.activo);
        entity.setDescripcion(this.descripcion);
        return entity;
    }
}