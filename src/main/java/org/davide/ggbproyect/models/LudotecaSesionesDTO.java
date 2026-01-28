package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LudotecaSesionesDTO {
    private Integer id;

    @NotNull
    private Integer idSesion;

    private Integer numAdultos;

    private Integer numNinos613;

    private Integer numNinos05;

    @NotNull
    private BigDecimal importeTotal;

    private Integer idComandaLudoteca;

    private Instant fechaCalculo;

    private String notas;

    public LudotecaSesionesDTO(LudotecaSesiones entity) {
        this.id = entity.getId();
        this.idSesion = entity.getIdSesion() != null ? entity.getIdSesion().getId() : null;
        this.numAdultos = entity.getNumAdultos();
        this.numNinos613 = entity.getNumNinos613();
        this.numNinos05 = entity.getNumNinos05();
        this.importeTotal = entity.getImporteTotal();
        this.idComandaLudoteca = entity.getIdComandaLudoteca() != null ? entity.getIdComandaLudoteca().getId() : null;
        this.fechaCalculo = entity.getFechaCalculo();
        this.notas = entity.getNotas();
    }

    public LudotecaSesiones toEntity() {
        LudotecaSesiones entity = new LudotecaSesiones();
        entity.setId(this.id);
        
        if (this.idSesion != null) {
            SesionesMesa sesion = new SesionesMesa();
            sesion.setId(this.idSesion);
            entity.setIdSesion(sesion);
        }

        entity.setNumAdultos(this.numAdultos);
        entity.setNumNinos613(this.numNinos613);
        entity.setNumNinos05(this.numNinos05);
        entity.setImporteTotal(this.importeTotal);
        
        if (this.idComandaLudoteca != null) {
            Comanda comanda = new Comanda();
            comanda.setId(this.idComandaLudoteca);
            entity.setIdComandaLudoteca(comanda);
        }

        entity.setFechaCalculo(this.fechaCalculo);
        entity.setNotas(this.notas);
        return entity;
    }
}