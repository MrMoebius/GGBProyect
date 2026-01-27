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
public class SesionesMesaDTO {
    private Integer id;

    @NotNull
    private Integer idMesa;

    private Integer idReserva;

    private Integer idEmpleadoApertura;

    @NotNull
    private Instant fechaHoraApertura;

    private Instant fechaHoraCierre;

    @Size(max = 30)
    private String estado;

    public SesionesMesaDTO(SesionesMesa entity) {
        this.id = entity.getId();
        this.idMesa = entity.getIdMesa() != null ? entity.getIdMesa().getId() : null;
        this.idReserva = entity.getIdReserva() != null ? entity.getIdReserva().getId() : null;
        this.idEmpleadoApertura = entity.getIdEmpleadoApertura() != null ? entity.getIdEmpleadoApertura().getId() : null;
        this.fechaHoraApertura = entity.getFechaHoraApertura();
        this.fechaHoraCierre = entity.getFechaHoraCierre();
        this.estado = entity.getEstado();
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

        entity.setFechaHoraApertura(this.fechaHoraApertura);
        entity.setFechaHoraCierre(this.fechaHoraCierre);
        entity.setEstado(this.estado);
        return entity;
    }
}