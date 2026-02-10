package org.davide.ggbproyect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifasLudotecaDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String nombreTramo;

    @NotNull
    @Min(0)
    private Integer edadMin;

    @NotNull
    @Min(0)
    private Integer edadMax;

    @NotNull
    @Positive
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