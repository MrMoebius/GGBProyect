package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "lineas_comanda")
@Data
public class LineasComanda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_linea", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_comanda", nullable = false)
    private Comanda idComanda;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto idProducto;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Column(name = "precio_unitario_historico", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitarioHistorico;

    @Size(max = 30)
    @ColumnDefault("'PENDIENTE'")
    @Column(name = "estado_preparacion", length = 30)
    private String estadoPreparacion;

    @Size(max = 255)
    @Column(name = "notas_chef")
    private String notasChef;

}