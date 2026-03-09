package org.davide.ggbproyect.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BggGameDetailsDTO {
    private Integer bggId;
    private String nombre;
    private Integer minJugadores;
    private Integer maxJugadores;
    private Integer duracionMediaMin;
    private String complejidad;
    private String genero;
    private String descripcion;
    private String imageUrl;
    private Double rawWeight;
    private Integer yearPublished;
}
