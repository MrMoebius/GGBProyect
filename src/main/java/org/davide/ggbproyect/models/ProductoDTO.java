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
public class ProductoDTO {
    private Integer id;

    @Size(max = 150)
    @NotNull
    private String nombre;

    private String descripcion;

    @Size(max = 50)
    @NotNull
    private String categoria;

    @NotNull
    private BigDecimal precio;

    private Boolean activo;

    public ProductoDTO(Producto entity) {
        this.id = entity.getId();
        this.nombre = entity.getNombre();
        this.descripcion = entity.getDescripcion();
        this.categoria = entity.getCategoria();
        this.precio = entity.getPrecio();
        this.activo = entity.getActivo();
    }

    public Producto toEntity() {
        Producto entity = new Producto();
        entity.setId(this.id);
        entity.setNombre(this.nombre);
        entity.setDescripcion(this.descripcion);
        entity.setCategoria(this.categoria);
        entity.setPrecio(this.precio);
        entity.setActivo(this.activo);
        return entity;
    }
}