package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "pagos_mesa")
@Data
public class PagosMesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sesion", nullable = false)
    private SesionesMesa idSesion;

    @NotNull
    @Column(name = "fecha_hora", nullable = false)
    private Instant fechaHora;

    @NotNull
    @Column(name = "importe", nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @Size(max = 50)
    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Size(max = 30)
    @ColumnDefault("'PENDIENTE'")
    @Column(name = "estado", length = 30)
    private String estado;

}