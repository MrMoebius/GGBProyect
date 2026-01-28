package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "mesas")
@Data
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesa", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "numero_mesa", nullable = false)
    private Integer numeroMesa;

    @NotNull
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Size(max = 50)
    @Column(name = "zona", length = 50)
    private String zona;

    @Size(max = 30)
    @ColumnDefault("'LIBRE'")
    @Column(name = "estado", length = 30)
    private String estado;

}