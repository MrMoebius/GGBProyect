package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
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