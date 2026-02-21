package org.davide.ggbproyect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.davide.ggbproyect.models.enums.EstadoEvento;
import org.davide.ggbproyect.models.enums.TipoEvento;
import org.davide.ggbproyect.validation.ValidEnum;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @NotBlank
    @Size(max = 150)
    private String titulo;

    private String descripcion;

    @NotNull
    private LocalDate fecha;

    @NotNull
    @Size(max = 5)
    private String hora;

    @Size(max = 5)
    private String horaFin;

    @NotBlank
    @Size(max = 100)
    private String ubicacion;

    @NotNull
    @Min(1)
    private Integer capacidad;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer inscritos;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer listaEspera;

    @Size(max = 20)
    @ValidEnum(enumClass = TipoEvento.class, message = "Valor de tipo invalido")
    private String tipo;

    @Size(max = 20)
    @ValidEnum(enumClass = EstadoEvento.class, message = "Valor de estado invalido")
    private String estado;

    private List<String> tags;

    @Size(max = 100)
    private String creadoPor;

    public EventoDTO(Evento entity) {
        this.id = entity.getId();
        this.titulo = entity.getTitulo();
        this.descripcion = entity.getDescripcion();
        this.fecha = entity.getFecha();
        this.hora = entity.getHora();
        this.horaFin = entity.getHoraFin();
        this.ubicacion = entity.getUbicacion();
        this.capacidad = entity.getCapacidad();
        this.tipo = entity.getTipo() != null ? entity.getTipo().name() : null;
        this.estado = entity.getEstado() != null ? entity.getEstado().name() : null;
        this.tags = entity.getTags() != null && !entity.getTags().isEmpty()
                ? Arrays.asList(entity.getTags().split(","))
                : List.of();
        this.creadoPor = entity.getCreadoPor();
        this.inscritos = 0;
        this.listaEspera = 0;
    }

    public Evento toEntity() {
        Evento entity = new Evento();
        entity.setId(this.id);
        entity.setTitulo(this.titulo);
        entity.setDescripcion(this.descripcion);
        entity.setFecha(this.fecha);
        entity.setHora(this.hora);
        entity.setHoraFin(this.horaFin);
        entity.setUbicacion(this.ubicacion);
        entity.setCapacidad(this.capacidad);
        if (this.tipo != null) {
            entity.setTipo(TipoEvento.valueOf(this.tipo));
        }
        if (this.estado != null) {
            entity.setEstado(EstadoEvento.valueOf(this.estado));
        } else {
            entity.setEstado(EstadoEvento.PROXIMO);
        }
        entity.setTags(this.tags != null ? String.join(",", this.tags) : null);
        entity.setCreadoPor(this.creadoPor);
        return entity;
    }
}
