package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "peticiones_pago")
@Data
public class PeticionesPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_peticion", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sesion", nullable = false)
    private SesionesMesa idSesion;

    @Size(max = 20)
    @Column(name = "metodo_preferido", length = 20)
    private String metodoPreferido;

    @ColumnDefault("0")
    @Column(name = "atendida")
    private Boolean atendida;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_peticion")
    private Instant fechaPeticion;

}