package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.davide.ggbproyect.models.enums.EstadoMesa;
import org.davide.ggbproyect.models.enums.UbicacionJuego;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "mesas")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesa", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "numero_mesa", nullable = false, unique = true)
    private Integer numeroMesa;

    @Size(max = 50)
    @NotNull
    @Column(name = "nombre_mesa", nullable = false, length = 50)
    private String nombreMesa;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Size(max = 50)
    @Column(name = "zona", length = 50)
    private String zona;

    @Enumerated(EnumType.STRING)
    @Column(name = "ubicacion", length = 50)
    private UbicacionJuego ubicacion;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'LIBRE'")
    @Column(name = "estado", length = 20)
    private EstadoMesa estado;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Mesa mesa = (Mesa) o;
        return getId() != null && Objects.equals(getId(), mesa.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
