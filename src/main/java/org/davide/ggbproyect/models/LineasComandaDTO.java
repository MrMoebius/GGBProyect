package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineasComandaDTO {
    private Integer id;

    @NotNull
    private Integer idComanda;

    @NotNull
    private Integer idProducto;

    @NotNull
    private Integer cantidad;

    @NotNull
    private BigDecimal precioUnitarioHistorico;

    @Size(max = 30)
    private String estadoPreparacion;

    @Size(max = 255)
    private String notasChef;

    public LineasComandaDTO(LineasComanda entity) {
        this.id = entity.getId();
        this.idComanda = entity.getIdComanda() != null ? entity.getIdComanda().getId() : null;
        this.idProducto = entity.getIdProducto() != null ? entity.getIdProducto().getId() : null;
        this.cantidad = entity.getCantidad();
        this.precioUnitarioHistorico = entity.getPrecioUnitarioHistorico();
        this.estadoPreparacion = entity.getEstadoPreparacion();
        this.notasChef = entity.getNotasChef();
    }

    public LineasComanda toEntity() {
        LineasComanda entity = new LineasComanda();
        entity.setId(this.id);
        
        if (this.idComanda != null) {
            Comanda comanda = new Comanda();
            comanda.setId(this.idComanda);
            entity.setIdComanda(comanda);
        }
        
        if (this.idProducto != null) {
            Producto producto = new Producto();
            producto.setId(this.idProducto);
            entity.setIdProducto(producto);
        }

        entity.setCantidad(this.cantidad);
        entity.setPrecioUnitarioHistorico(this.precioUnitarioHistorico);
        entity.setEstadoPreparacion(this.estadoPreparacion);
        entity.setNotasChef(this.notasChef);
        return entity;
    }
}