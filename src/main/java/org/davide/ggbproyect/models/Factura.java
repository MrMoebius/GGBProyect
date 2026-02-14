package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.davide.ggbproyect.models.enums.EstadoFactura;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "facturas", indexes = {
    @Index(name = "idx_factura_cliente", columnList = "id_cliente"),
    @Index(name = "idx_factura_fecha", columnList = "fecha_emision")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_factura_numero", columnNames = "numero_factura"),
    @UniqueConstraint(name = "uk_factura_sesion", columnNames = "id_sesion")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura", nullable = false)
    private Integer id;

    @Version
    @Column(name = "version")
    private Long version;

    @Size(max = 20)
    @NotNull
    @Column(name = "numero_factura", nullable = false, length = 20)
    private String numeroFactura;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sesion", nullable = false)
    @ToString.Exclude
    private SesionesMesa idSesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_cliente")
    @ToString.Exclude
    private Cliente idCliente;

    @NotNull
    @Column(name = "fecha_emision", nullable = false)
    private Instant fechaEmision;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "base_imponible_10", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseImponible10;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "cuota_iva_10", nullable = false, precision = 10, scale = 2)
    private BigDecimal cuotaIva10;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "base_imponible_21", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseImponible21;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "cuota_iva_21", nullable = false, precision = 10, scale = 2)
    private BigDecimal cuotaIva21;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "importe_ludoteca", nullable = false, precision = 10, scale = 2)
    private BigDecimal importeLudoteca;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "total_pagado", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPagado;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'EMITIDA'")
    @Column(name = "estado", length = 20)
    private EstadoFactura estado;

    @Lob
    @Column(name = "notas")
    private String notas;

    @PrePersist
    protected void onCreate() {
        if (fechaEmision == null) {
            fechaEmision = Instant.now();
        }
        if (estado == null) {
            estado = EstadoFactura.EMITIDA;
        }
        if (baseImponible10 == null) baseImponible10 = BigDecimal.ZERO;
        if (cuotaIva10 == null) cuotaIva10 = BigDecimal.ZERO;
        if (baseImponible21 == null) baseImponible21 = BigDecimal.ZERO;
        if (cuotaIva21 == null) cuotaIva21 = BigDecimal.ZERO;
        if (importeLudoteca == null) importeLudoteca = BigDecimal.ZERO;
        if (total == null) total = BigDecimal.ZERO;
        if (totalPagado == null) totalPagado = BigDecimal.ZERO;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Factura factura = (Factura) o;
        return getId() != null && Objects.equals(getId(), factura.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
