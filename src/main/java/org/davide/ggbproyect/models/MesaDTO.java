package org.davide.ggbproyect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.davide.ggbproyect.models.enums.EstadoMesa;
import org.davide.ggbproyect.models.enums.UbicacionJuego;
import org.davide.ggbproyect.validation.ValidEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MesaDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @NotNull
    private Integer numeroMesa;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String nombreMesa;

    @NotNull
    @Min(1)
    private Integer capacidad;

    @Size(max = 50)
    private String zona;

    @Size(max = 50)
    @ValidEnum(enumClass = UbicacionJuego.class, message = "Valor de ubicacion invalido")
    private String ubicacion;

    @Size(max = 20)
    @ValidEnum(enumClass = EstadoMesa.class, message = "Valor de estado invalido")
    private String estado;

    public MesaDTO(Mesa entity) {
        this.id = entity.getId();
        this.numeroMesa = entity.getNumeroMesa();
        this.nombreMesa = entity.getNombreMesa();
        this.capacidad = entity.getCapacidad();
        this.zona = entity.getZona();
        this.ubicacion = entity.getUbicacion() != null ? entity.getUbicacion().name() : null;
        this.estado = entity.getEstado() != null ? entity.getEstado().name() : null;
    }

    public Mesa toEntity() {
        Mesa entity = new Mesa();
        entity.setId(this.id);
        entity.setNumeroMesa(this.numeroMesa);
        entity.setNombreMesa(this.nombreMesa);
        entity.setCapacidad(this.capacidad);
        entity.setZona(this.zona);
        if (this.ubicacion != null) {
            try {
                entity.setUbicacion(UbicacionJuego.valueOf(this.ubicacion));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de ubicacion invalido: " + this.ubicacion);
            }
        }
        if (this.estado != null) {
            try {
                entity.setEstado(EstadoMesa.valueOf(this.estado));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de estado invalido: " + this.estado);
            }
        }
        return entity;
    }
}
