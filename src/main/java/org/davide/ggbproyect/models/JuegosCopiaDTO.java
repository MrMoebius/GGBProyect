package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.davide.ggbproyect.models.enums.EstadoCopiaJuego;

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
        this.estado = entity.getEstado() != null ? entity.getEstado().name() : null;
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
        if (this.estado != null) {
            try {
                entity.setEstado(EstadoCopiaJuego.valueOf(this.estado));
            } catch (IllegalArgumentException e) {
                // Handle invalid enum
            }
        }
        return entity;
    }
}