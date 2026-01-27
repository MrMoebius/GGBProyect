package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuegosCopiaDTO {
    private Integer id;

    @NotNull
    private Integer idJuego;

    @Size(max = 50)
    private String codigoInterno;

    @Size(max = 30)
    private String estado;

    public JuegosCopiaDTO(JuegosCopia entity) {
        this.id = entity.getId();
        this.idJuego = entity.getIdJuego() != null ? entity.getIdJuego().getId() : null;
        this.codigoInterno = entity.getCodigoInterno();
        this.estado = entity.getEstado();
    }

    public JuegosCopia toEntity() {
        JuegosCopia entity = new JuegosCopia();
        entity.setId(this.id);
        
        if (this.idJuego != null) {
            Juego juego = new Juego();
            juego.setId(this.idJuego);
            entity.setIdJuego(juego);
        }

        entity.setCodigoInterno(this.codigoInterno);
        entity.setEstado(this.estado);
        return entity;
    }
}