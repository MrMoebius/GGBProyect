package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.davide.ggbproyect.models.enums.EstadoEvento;
import org.davide.ggbproyect.models.enums.TipoEvento;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "eventos")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Version
    @Column(name = "version")
    private Long version;

    @NotNull
    @Size(max = 150)
    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Lob
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Size(max = 5)
    @Column(name = "hora", nullable = false, length = 5)
    private String hora;

    @Size(max = 5)
    @Column(name = "hora_fin", length = 5)
    private String horaFin;

    @NotNull
    @Size(max = 100)
    @Column(name = "ubicacion", nullable = false, length = 100)
    private String ubicacion;

    @NotNull
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 20)
    private TipoEvento tipo;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PROXIMO'")
    @Column(name = "estado", length = 20)
    private EstadoEvento estado;

    @Size(max = 500)
    @Column(name = "tags", length = 500)
    private String tags;

    @Size(max = 100)
    @Column(name = "creado_por", length = 100)
    private String creadoPor;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Evento evento = (Evento) o;
        return getId() != null && Objects.equals(getId(), evento.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
