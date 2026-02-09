package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.davide.ggbproyect.models.enums.EstadoSesion;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "sesiones_mesa", indexes = {
    @Index(name = "idx_sesion_mesa", columnList = "id_mesa"),
    @Index(name = "idx_sesion_reserva", columnList = "id_reserva"),
    @Index(name = "idx_sesion_empleado", columnList = "id_empleado_apertura")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SesionesMesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion", nullable = false)
    private Integer id;

    @Version
    @Column(name = "version")
    private Long version;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_mesa", nullable = false)
    @ToString.Exclude
    private Mesa idMesa;

    @Column(name = "inicio")
    private Instant inicio;

    @Column(name = "fin")
    private Instant fin;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVA'")
    @Column(name = "estado", length = 20)
    private EstadoSesion estado;

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

    @PrePersist
    protected void onCreate() {
        if (fechaHoraApertura == null) {
            fechaHoraApertura = Instant.now();
        }
        if (estado == null) {
            estado = EstadoSesion.ACTIVA;
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        SesionesMesa that = (SesionesMesa) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
