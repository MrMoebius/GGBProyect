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
@Table(name = "reservas_mesa")
@Data
public class ReservasMesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_cliente")
    private Cliente idCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_mesa")
    private Mesa idMesa;

    @NotNull
    @Column(name = "fecha_hora_inicio", nullable = false)
    private Instant fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private Instant fechaHoraFin;

    @Column(name = "num_personas")
    private Integer numPersonas;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_juego_deseado")
    private Juego idJuegoDeseado;

    @Size(max = 30)
    @ColumnDefault("'PENDIENTE'")
    @Column(name = "estado", length = 30)
    private String estado;

    @Lob
    @Column(name = "notas")
    private String notas;

}