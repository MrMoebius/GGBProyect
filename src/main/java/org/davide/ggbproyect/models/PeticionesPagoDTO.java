package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.davide.ggbproyect.models.enums.MetodoPago;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeticionesPagoDTO {
    private Integer id;

    @NotNull
    private Integer idSesion;

    @Size(max = 20)
    private String metodoPreferido;

    private Boolean atendida;

    private Instant fechaPeticion;

    public PeticionesPagoDTO(PeticionesPago entity) {
        this.id = entity.getId();
        this.idSesion = entity.getIdSesion() != null ? entity.getIdSesion().getId() : null;
        this.metodoPreferido = entity.getMetodoPreferido() != null ? entity.getMetodoPreferido().name() : null;
        this.atendida = entity.getAtendida();
        this.fechaPeticion = entity.getFechaPeticion();
    }

    public PeticionesPago toEntity() {
        PeticionesPago entity = new PeticionesPago();
        entity.setId(this.id);
        
        if (this.idSesion != null) {
            SesionesMesa sesion = new SesionesMesa();
            sesion.setId(this.idSesion);
            entity.setIdSesion(sesion);
        }

        if (this.metodoPreferido != null) {
            try {
                entity.setMetodoPreferido(MetodoPago.valueOf(this.metodoPreferido));
            } catch (IllegalArgumentException e) {
                // Handle invalid enum
            }
        }
        entity.setAtendida(this.atendida);
        entity.setFechaPeticion(this.fechaPeticion);
        return entity;
    }
}