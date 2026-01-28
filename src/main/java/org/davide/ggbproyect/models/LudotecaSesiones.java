package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ludoteca_sesiones")
@Data
public class LudotecaSesiones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ludoteca_sesion", nullable = false)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sesion", nullable = false)
    private SesionesMesa idSesion;

    @ColumnDefault("0")
    @Column(name = "num_adultos")
    private Integer numAdultos;

    @ColumnDefault("0")
    @Column(name = "num_ninos_6_13")
    private Integer numNinos613;

    @ColumnDefault("0")
    @Column(name = "num_ninos_0_5")
    private Integer numNinos05;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "importe_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal importeTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_comanda_ludoteca")
    private Comanda idComandaLudoteca;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_calculo")
    private Instant fechaCalculo;

    @Lob
    @Column(name = "notas")
    private String notas;

}