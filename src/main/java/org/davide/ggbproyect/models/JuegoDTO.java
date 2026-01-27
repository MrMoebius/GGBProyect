package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Size(max = 50)
    private String complejidad;

    @Size(max = 100)
    private String genero;

    @Size(max = 50)
    private String idioma;

    private String descripcion;

    @Size(max = 100)
    private String ubicacion;

    private Boolean recomendadoDosJugadores;

    private Boolean activo;

    public JuegoDTO(Juego entity) {
        this.id = entity.getId();
        this.nombre = entity.getNombre();
        this.minJugadores = entity.getMinJugadores();
        this.maxJugadores = entity.getMaxJugadores();
        this.duracionMediaMin = entity.getDuracionMediaMin();
        this.complejidad = entity.getComplejidad();
        this.genero = entity.getGenero();
        this.idioma = entity.getIdioma();
        this.descripcion = entity.getDescripcion();
        this.ubicacion = entity.getUbicacion();
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
        entity.setComplejidad(this.complejidad);
        entity.setGenero(this.genero);
        entity.setIdioma(this.idioma);
        entity.setDescripcion(this.descripcion);
        entity.setUbicacion(this.ubicacion);
        entity.setRecomendadoDosJugadores(this.recomendadoDosJugadores);
        entity.setActivo(this.activo);
        return entity;
    }
}