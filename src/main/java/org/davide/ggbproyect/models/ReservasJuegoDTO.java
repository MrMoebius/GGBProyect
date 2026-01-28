package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservasJuegoDTO {
    private Integer id;

    @NotNull
    private Integer idSesion;

    @NotNull
    private Integer idCopia;

    private Instant horaInicio;

    private Instant horaFin;

    @Size(max = 30)
    private String estado;

    public ReservasJuegoDTO(ReservasJuego entity) {
        this.id = entity.getId();
        this.idSesion = entity.getIdSesion() != null ? entity.getIdSesion().getId() : null;
        this.idCopia = entity.getIdCopia() != null ? entity.getIdCopia().getId() : null;
        this.horaInicio = entity.getHoraInicio();
        this.horaFin = entity.getHoraFin();
        this.estado = entity.getEstado();
    }

    public ReservasJuego toEntity() {
        ReservasJuego entity = new ReservasJuego();
        entity.setId(this.id);
        
        if (this.idSesion != null) {
            SesionesMesa sesion = new SesionesMesa();
            sesion.setId(this.idSesion);
            entity.setIdSesion(sesion);
        }
        
        if (this.idCopia != null) {
            JuegosCopia copia = new JuegosCopia();
            copia.setId(this.idCopia);
            entity.setIdCopia(copia);
        }

        entity.setHoraInicio(this.horaInicio);
        entity.setHoraFin(this.horaFin);
        entity.setEstado(this.estado);
        return entity;
    }
}