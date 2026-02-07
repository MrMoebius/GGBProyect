package org.davide.ggbproyect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.davide.ggbproyect.models.enums.EstadoSesion;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SesionesMesaDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @NotNull
    private Integer idMesa;

    private Integer idReserva; // opcional

    private Integer idEmpleadoApertura; // opcional

    private Instant inicio;

    private Instant fin;

    private String estado;

    private Instant fechaHoraApertura;

    private Instant fechaHoraCierre;

    public SesionesMesaDTO(SesionesMesa entity) {
        this.id = entity.getId();
        this.idMesa = entity.getIdMesa() != null ? entity.getIdMesa().getId() : null;
        this.idReserva = entity.getIdReserva() != null ? entity.getIdReserva().getId() : null;
        this.idEmpleadoApertura = entity.getIdEmpleadoApertura() != null ? entity.getIdEmpleadoApertura().getId() : null;
        this.inicio = entity.getInicio();
        this.fin = entity.getFin();
        this.estado = entity.getEstado() != null ? entity.getEstado().name() : null;
        this.fechaHoraApertura = entity.getFechaHoraApertura();
        this.fechaHoraCierre = entity.getFechaHoraCierre();
    }

    public SesionesMesa toEntity() {
        SesionesMesa entity = new SesionesMesa();
        entity.setId(this.id);
        
        if (this.idMesa != null) {
            Mesa mesa = new Mesa();
            mesa.setId(this.idMesa);
            entity.setIdMesa(mesa);
        }

        if (this.idReserva != null) {
            ReservasMesa reserva = new ReservasMesa();
            reserva.setId(this.idReserva);
            entity.setIdReserva(reserva);
        }

        if (this.idEmpleadoApertura != null) {
            Empleado empleado = new Empleado();
            empleado.setId(this.idEmpleadoApertura);
            entity.setIdEmpleadoApertura(empleado);
        }
        
        entity.setInicio(this.inicio);
        entity.setFin(this.fin);
        if (this.estado != null) {
            try {
                entity.setEstado(EstadoSesion.valueOf(this.estado));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de estado invalido: " + this.estado);
            }
        }
        entity.setFechaHoraApertura(this.fechaHoraApertura);
        entity.setFechaHoraCierre(this.fechaHoraCierre);
        return entity;
    }
}