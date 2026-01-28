package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "juegos")
@Data
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

    @Size(max = 50)
    @Column(name = "complejidad", length = 50)
    private String complejidad;

    @Size(max = 100)
    @Column(name = "genero", length = 100)
    private String genero;

    @Size(max = 50)
    @Column(name = "idioma", length = 50)
    private String idioma;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Size(max = 100)
    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @ColumnDefault("0")
    @Column(name = "recomendado_dos_jugadores")
    private Boolean recomendadoDosJugadores;

    @ColumnDefault("1")
    @Column(name = "activo")
    private Boolean activo;

}