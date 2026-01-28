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
public class ReservasMesaDTO {
    private Integer id;

    private Integer idCliente;

    private Integer idMesa;

    @NotNull
    private Instant fechaHoraInicio;

    private Instant fechaHoraFin;

    private Integer numPersonas;

    private Integer idJuegoDeseado;

    @Size(max = 30)
    private String estado;

    private String notas;

    public ReservasMesaDTO(ReservasMesa entity) {
        this.id = entity.getId();
        this.idCliente = entity.getIdCliente() != null ? entity.getIdCliente().getId() : null;
        this.idMesa = entity.getIdMesa() != null ? entity.getIdMesa().getId() : null;
        this.fechaHoraInicio = entity.getFechaHoraInicio();
        this.fechaHoraFin = entity.getFechaHoraFin();
        this.numPersonas = entity.getNumPersonas();
        this.idJuegoDeseado = entity.getIdJuegoDeseado() != null ? entity.getIdJuegoDeseado().getId() : null;
        this.estado = entity.getEstado();
        this.notas = entity.getNotas();
    }

    public ReservasMesa toEntity() {
        ReservasMesa entity = new ReservasMesa();
        entity.setId(this.id);
        
        if (this.idCliente != null) {
            Cliente cliente = new Cliente();
            cliente.setId(this.idCliente);
            entity.setIdCliente(cliente);
        }
        
        if (this.idMesa != null) {
            Mesa mesa = new Mesa();
            mesa.setId(this.idMesa);
            entity.setIdMesa(mesa);
        }

        entity.setFechaHoraInicio(this.fechaHoraInicio);
        entity.setFechaHoraFin(this.fechaHoraFin);
        entity.setNumPersonas(this.numPersonas);
        
        if (this.idJuegoDeseado != null) {
            Juego juego = new Juego();
            juego.setId(this.idJuegoDeseado);
            entity.setIdJuegoDeseado(juego);
        }

        entity.setEstado(this.estado);
        entity.setNotas(this.notas);
        return entity;
    }
}