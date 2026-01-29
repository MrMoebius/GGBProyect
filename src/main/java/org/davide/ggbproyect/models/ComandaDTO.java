package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComandaDTO {
    private Integer id;

    @NotNull
    private Integer idSesion;

    private LocalDateTime fechaHora;

    @Size(max = 20)
    private String estado;

    private BigDecimal total;

    public ComandaDTO(Comanda entity) {
        this.id = entity.getId();
        this.idSesion = entity.getIdSesion() != null ? entity.getIdSesion().getId() : null;
        this.fechaHora = entity.getFechaHora();
        this.estado = entity.getEstado();
        this.total = entity.getTotal();
    }

    public Comanda toEntity() {
        Comanda entity = new Comanda();
        entity.setId(this.id);
        
        if (this.idSesion != null) {
            SesionesMesa sesion = new SesionesMesa();
            sesion.setId(this.idSesion);
            entity.setIdSesion(sesion);
        }
        
        entity.setFechaHora(this.fechaHora);
        entity.setEstado(this.estado);
        entity.setTotal(this.total);
        return entity;
    }
}