package org.davide.ggbproyect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.davide.ggbproyect.models.enums.EstadoFactura;
import org.davide.ggbproyect.validation.ValidEnum;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String numeroFactura;

    @NotNull
    private Integer idSesion;

    private Integer idCliente;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant fechaEmision;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal baseImponible10;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal cuotaIva10;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal baseImponible21;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal cuotaIva21;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal importeLudoteca;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal total;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalPagado;

    @ValidEnum(enumClass = EstadoFactura.class, message = "Valor de estado invalido")
    private String estado;

    private String notas;

    public FacturaDTO(Factura entity) {
        this.id = entity.getId();
        this.numeroFactura = entity.getNumeroFactura();
        this.idSesion = entity.getIdSesion() != null ? entity.getIdSesion().getId() : null;
        this.idCliente = entity.getIdCliente() != null ? entity.getIdCliente().getId() : null;
        this.fechaEmision = entity.getFechaEmision();
        this.baseImponible10 = entity.getBaseImponible10();
        this.cuotaIva10 = entity.getCuotaIva10();
        this.baseImponible21 = entity.getBaseImponible21();
        this.cuotaIva21 = entity.getCuotaIva21();
        this.importeLudoteca = entity.getImporteLudoteca();
        this.total = entity.getTotal();
        this.totalPagado = entity.getTotalPagado();
        this.estado = entity.getEstado() != null ? entity.getEstado().name() : null;
        this.notas = entity.getNotas();
    }

    public Factura toEntity() {
        Factura entity = new Factura();
        entity.setId(this.id);
        entity.setNumeroFactura(this.numeroFactura);

        if (this.idSesion != null) {
            SesionesMesa sesion = new SesionesMesa();
            sesion.setId(this.idSesion);
            entity.setIdSesion(sesion);
        }

        if (this.idCliente != null) {
            Cliente cliente = new Cliente();
            cliente.setId(this.idCliente);
            entity.setIdCliente(cliente);
        }

        entity.setFechaEmision(this.fechaEmision);
        entity.setBaseImponible10(this.baseImponible10);
        entity.setCuotaIva10(this.cuotaIva10);
        entity.setBaseImponible21(this.baseImponible21);
        entity.setCuotaIva21(this.cuotaIva21);
        entity.setImporteLudoteca(this.importeLudoteca);
        entity.setTotal(this.total);
        entity.setTotalPagado(this.totalPagado);
        if (this.estado != null) {
            try {
                entity.setEstado(EstadoFactura.valueOf(this.estado));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de estado invalido: " + this.estado);
            }
        }
        entity.setNotas(this.notas);
        return entity;
    }
}
