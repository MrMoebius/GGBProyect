package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", nullable = false)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Size(max = 50)
    @NotNull
    @Column(name = "categoria", nullable = false, length = 50)
    private String categoria;

    @NotNull
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @ColumnDefault("1")
    @Column(name = "activo")
    private Boolean activo;

}