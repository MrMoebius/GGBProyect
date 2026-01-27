package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "tarifas_ludoteca")
@Data
public class TarifasLudoteca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nombre_tramo", nullable = false, length = 50)
    private String nombreTramo;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "edad_min", nullable = false)
    private Integer edadMin;

    @NotNull
    @ColumnDefault("99")
    @Column(name = "edad_max", nullable = false)
    private Integer edadMax;

    @NotNull
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @ColumnDefault("1")
    @Column(name = "activo")
    private Boolean activo;

    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;

}