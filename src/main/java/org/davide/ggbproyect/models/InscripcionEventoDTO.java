package org.davide.ggbproyect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionEventoDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    private Integer idEvento;

    private String emailUsuario;

    private String estado;

    private Instant fechaInscripcion;

    public InscripcionEventoDTO(InscripcionEvento entity) {
        this.id = entity.getId();
        this.idEvento = entity.getIdEvento() != null ? entity.getIdEvento().getId() : null;
        this.emailUsuario = entity.getEmailUsuario();
        this.estado = entity.getEstado() != null ? entity.getEstado().name() : null;
        this.fechaInscripcion = entity.getFechaInscripcion();
    }
}
