package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "ludoteca_sesiones")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class LudotecaSesiones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ludoteca_sesion", nullable = false)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sesion", nullable = false)
    @ToString.Exclude
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
    @ToString.Exclude
    private Comanda idComandaLudoteca;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_calculo")
    private Instant fechaCalculo;

    @Lob
    @Column(name = "notas")
    private String notas;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        LudotecaSesiones that = (LudotecaSesiones) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}