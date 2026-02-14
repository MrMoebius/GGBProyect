package org.davide.ggbproyect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @NotBlank
    private String nombre;

    @Size(min = 1, max = 250)
    private String descripcion;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String categoria;

    @NotNull
    @PositiveOrZero
    private BigDecimal precio;

    private Boolean activo;

    @NotNull
    private Integer tipoIva;

    public ProductoDTO(Producto entity) {
        this.id = entity.getId();
        this.nombre = entity.getNombre();
        this.descripcion = entity.getDescripcion();
        this.categoria = entity.getCategoria();
        this.precio = entity.getPrecio();
        this.activo = entity.getActivo();
        this.tipoIva = entity.getTipoIva();
    }

    public Producto toEntity() {
        Producto entity = new Producto();
        entity.setId(this.id);
        entity.setNombre(this.nombre);
        entity.setDescripcion(this.descripcion);
        entity.setCategoria(this.categoria);
        entity.setPrecio(this.precio);
        entity.setActivo(this.activo);
        entity.setTipoIva(this.tipoIva);
        return entity;
    }
}