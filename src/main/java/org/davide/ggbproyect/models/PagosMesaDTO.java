package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagosMesaDTO {
    private Integer id;

    @NotNull
    private Integer idSesion;

    @NotNull
    private Instant fechaHora;

    @NotNull
    private BigDecimal importe;

    @Size(max = 50)
    private String metodoPago;

    @Size(max = 30)
    private String estado;

    public PagosMesaDTO(PagosMesa entity) {
        this.id = entity.getId();
        this.idSesion = entity.getIdSesion() != null ? entity.getIdSesion().getId() : null;
        this.fechaHora = entity.getFechaHora();
        this.importe = entity.getImporte();
        this.metodoPago = entity.getMetodoPago();
        this.estado = entity.getEstado();
    }

    public PagosMesa toEntity() {
        PagosMesa entity = new PagosMesa();
        entity.setId(this.id);
        
        if (this.idSesion != null) {
            SesionesMesa sesion = new SesionesMesa();
            sesion.setId(this.idSesion);
            entity.setIdSesion(sesion);
        }

        entity.setFechaHora(this.fechaHora);
        entity.setImporte(this.importe);
        entity.setMetodoPago(this.metodoPago);
        entity.setEstado(this.estado);
        return entity;
    }
}