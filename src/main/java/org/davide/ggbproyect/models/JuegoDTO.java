package org.davide.ggbproyect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.davide.ggbproyect.models.enums.ComplejidadJuego;
import org.davide.ggbproyect.models.enums.IdiomaJuego;
import org.davide.ggbproyect.models.enums.UbicacionJuego;
import org.davide.ggbproyect.validation.ValidEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuegoDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @NotBlank
    private String nombre;

    @Min(1)
    private Integer minJugadores;

    private Integer maxJugadores;

    @Min(1)
    private Integer duracionMediaMin;

    @ValidEnum(enumClass = ComplejidadJuego.class, message = "Valor de complejidad invalido")
    private String complejidad;

    @Size(max = 255)
    private String genero;

    @ValidEnum(enumClass = IdiomaJuego.class, message = "Valor de idioma invalido")
    private String idioma;

    private String descripcion;

    private String observaciones;

    @ValidEnum(enumClass = UbicacionJuego.class, message = "Valor de ubicacion invalido")
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
        this.observaciones = entity.getObservaciones();
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
                throw new IllegalArgumentException("Valor de complejidad invalido: " + this.complejidad);
            }
        }
        entity.setGenero(this.genero);
        if (this.idioma != null) {
            try {
                entity.setIdioma(IdiomaJuego.valueOf(this.idioma));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de idioma invalido: " + this.idioma);
            }
        }
        entity.setDescripcion(this.descripcion);
        entity.setObservaciones(this.observaciones);
        if (this.ubicacion != null) {
            try {
                entity.setUbicacion(UbicacionJuego.valueOf(this.ubicacion));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de ubicacion invalido: " + this.ubicacion);
            }
        }
        entity.setRecomendadoDosJugadores(this.recomendadoDosJugadores);
        entity.setActivo(this.activo);
        return entity;
    }
}