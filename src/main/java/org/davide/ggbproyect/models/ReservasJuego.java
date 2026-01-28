package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "reservas_juegos")
@Data
public class ReservasJuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva_juego", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sesion", nullable = false)
    private SesionesMesa idSesion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_copia", nullable = false)
    private JuegosCopia idCopia;

    @Column(name = "hora_inicio")
    private Instant horaInicio;

    @Column(name = "hora_fin")
    private Instant horaFin;

    @Size(max = 30)
    @ColumnDefault("'ACTIVA'")
    @Column(name = "estado", length = 30)
    private String estado;

}