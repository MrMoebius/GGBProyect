package org.davide.ggbproyect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.davide.ggbproyect.models.enums.EstadoPago;
import org.davide.ggbproyect.models.enums.MetodoPago;
import org.davide.ggbproyect.validation.ValidEnum;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagosMesaDTO
{

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @NotNull
    private Integer idSesion;

    @NotNull
    private Instant fechaHora;

    @NotNull
    @Positive
    private BigDecimal importe;

    @Size(max = 50)
    @ValidEnum(enumClass = MetodoPago.class, message = "Valor de metodo de pago invalido")
    private String metodoPago;

    @Size(max = 30)
    @ValidEnum(enumClass = EstadoPago.class, message = "Valor de estado invalido")
    private String estado;

    public PagosMesaDTO(PagosMesa entity) {
        this.id = entity.getId();
        this.idSesion = entity.getIdSesion() != null ? entity.getIdSesion().getId() : null;
        this.fechaHora = entity.getFechaHora();
        this.importe = entity.getImporte();
        this.metodoPago = entity.getMetodoPago() != null ? entity.getMetodoPago().name() : null;
        this.estado = entity.getEstado() != null ? entity.getEstado().name() : null;
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
        if (this.metodoPago != null) {
            try {
                entity.setMetodoPago(MetodoPago.valueOf(this.metodoPago));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de metodo de pago invalido: " + this.metodoPago);
            }
        }
        if (this.estado != null) {
            try {
                entity.setEstado(EstadoPago.valueOf(this.estado));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de estado invalido: " + this.estado);
            }
        }
        return entity;
    }
}