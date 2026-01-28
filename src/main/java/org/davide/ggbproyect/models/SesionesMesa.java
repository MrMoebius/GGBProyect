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
@Table(name = "sesiones_mesa")
@Data
public class SesionesMesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_mesa", nullable = false)
    private Mesa idMesa;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_reserva")
    private ReservasMesa idReserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_empleado_apertura")
    private Empleado idEmpleadoApertura;

    @NotNull
    @Column(name = "fecha_hora_apertura", nullable = false)
    private Instant fechaHoraApertura;

    @Column(name = "fecha_hora_cierre")
    private Instant fechaHoraCierre;

    @Size(max = 30)
    @ColumnDefault("'ABIERTA'")
    @Column(name = "estado", length = 30)
    private String estado;

}