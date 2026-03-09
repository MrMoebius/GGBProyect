package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.davide.ggbproyect.models.enums.EstadoInscripcion;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "inscripciones_evento", indexes = {
    @Index(name = "idx_inscripcion_evento", columnList = "id_evento"),
    @Index(name = "idx_inscripcion_email", columnList = "email_usuario")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class InscripcionEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Version
    @Column(name = "version")
    private Long version;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    @ToString.Exclude
    private Evento idEvento;

    @NotNull
    @Size(max = 150)
    @Column(name = "email_usuario", nullable = false, length = 150)
    private String emailUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoInscripcion estado;

    @Column(name = "fecha_inscripcion")
    private Instant fechaInscripcion;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        InscripcionEvento that = (InscripcionEvento) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
