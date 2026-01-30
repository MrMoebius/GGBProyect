package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.davide.ggbproyect.models.enums.ComplejidadJuego;
import org.davide.ggbproyect.models.enums.IdiomaJuego;
import org.davide.ggbproyect.models.enums.UbicacionJuego;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "juegos")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_juego", nullable = false)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "min_jugadores")
    private Integer minJugadores;

    @Column(name = "max_jugadores")
    private Integer maxJugadores;

    @Column(name = "duracion_media_min")
    private Integer duracionMediaMin;

    @Enumerated(EnumType.STRING)
    @Column(name = "complejidad", length = 50)
    private ComplejidadJuego complejidad;

    @Size(max = 255)
    @Column(name = "genero", length = 255)
    private String genero; // String para guardar múltiples valores separados por coma

    @Enumerated(EnumType.STRING)
    @Column(name = "idioma", length = 50)
    private IdiomaJuego idioma;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ubicacion", length = 100)
    private UbicacionJuego ubicacion;

    @ColumnDefault("0")
    @Column(name = "recomendado_dos_jugadores")
    private Boolean recomendadoDosJugadores;

    @ColumnDefault("1")
    @Column(name = "activo")
    private Boolean activo;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Juego juego = (Juego) o;
        return getId() != null && Objects.equals(getId(), juego.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}