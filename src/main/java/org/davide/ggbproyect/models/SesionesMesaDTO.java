package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.davide.ggbproyect.models.enums.EstadoSesion;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SesionesMesaDTO {
    private Integer id;

    @NotNull
    private Integer idMesa;

    private LocalDateTime inicio;

    private LocalDateTime fin;

    @Size(max = 20)
    private String estado;

    public SesionesMesaDTO(SesionesMesa entity) {
        this.id = entity.getId();
        this.idMesa = entity.getIdMesa() != null ? entity.getIdMesa().getId() : null;
        this.inicio = entity.getInicio();
        this.fin = entity.getFin();
        this.estado = entity.getEstado() != null ? entity.getEstado().name() : null;
    }

    public SesionesMesa toEntity() {
        SesionesMesa entity = new SesionesMesa();
        entity.setId(this.id);
        
        if (this.idMesa != null) {
            Mesa mesa = new Mesa();
            mesa.setId(this.idMesa);
            entity.setIdMesa(mesa);
        }
        
        entity.setInicio(this.inicio);
        entity.setFin(this.fin);
        if (this.estado != null) {
            try {
                entity.setEstado(EstadoSesion.valueOf(this.estado));
            } catch (IllegalArgumentException e) {
                // Handle invalid enum
            }
        }
        return entity;
    }
}