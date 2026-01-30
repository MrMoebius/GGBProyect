package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.davide.ggbproyect.models.enums.ComplejidadJuego;
import org.davide.ggbproyect.models.enums.IdiomaJuego;
import org.davide.ggbproyect.models.enums.UbicacionJuego;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuegoDTO {
    private Integer id;

    @Size(max = 150)
    @NotNull
    private String nombre;

    private Integer minJugadores;

    private Integer maxJugadores;

    private Integer duracionMediaMin;

    private String complejidad;

    @Size(max = 255)
    private String genero;

    private String idioma;

    private String descripcion;

    private String ubicacion;

    private Boolean recomendadoDosJugadores;

    private Boolean activo;

    public JuegoDTO(Juego entity) {
        this.id = entity.getId();
        this.nombre = entity.getNombre();
        this.minJugadores = entity.getMinJugadores();
        this.maxJugadores = entity.getMaxJugadores();
        this.duracionMediaMin = entity.getDuracionMediaMin();
        this.complejidad = entity.getComplejidad() != null ? entity.getComplejidad().name() : null;
        this.genero = entity.getGenero();
        this.idioma = entity.getIdioma() != null ? entity.getIdioma().name() : null;
        this.descripcion = entity.getDescripcion();
        this.ubicacion = entity.getUbicacion() != null ? entity.getUbicacion().name() : null;
        this.recomendadoDosJugadores = entity.getRecomendadoDosJugadores();
        this.activo = entity.getActivo();
    }

    public Juego toEntity() {
        Juego entity = new Juego();
        entity.setId(this.id);
        entity.setNombre(this.nombre);
        entity.setMinJugadores(this.minJugadores);
        entity.setMaxJugadores(this.maxJugadores);
        entity.setDuracionMediaMin(this.duracionMediaMin);
        if (this.complejidad != null) {
            try {
                entity.setComplejidad(ComplejidadJuego.valueOf(this.complejidad));
            } catch (IllegalArgumentException e) {
                // Handle invalid enum value or leave null
            }
        }
        entity.setGenero(this.genero);
        if (this.idioma != null) {
            try {
                entity.setIdioma(IdiomaJuego.valueOf(this.idioma));
            } catch (IllegalArgumentException e) {
                // Handle invalid enum value
            }
        }
        entity.setDescripcion(this.descripcion);
        if (this.ubicacion != null) {
            try {
                entity.setUbicacion(UbicacionJuego.valueOf(this.ubicacion));
            } catch (IllegalArgumentException e) {
                // Handle invalid enum value
            }
        }
        entity.setRecomendadoDosJugadores(this.recomendadoDosJugadores);
        entity.setActivo(this.activo);
        return entity;
    }
}