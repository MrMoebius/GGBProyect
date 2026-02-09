package org.davide.ggbproyect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @NotNull
    @NotBlank
    @Size(max = 150)
    private String nombre;

    @NotNull
    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String telefono;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, max = 100)
    private String password;

    private LocalDate fechaAlta;

    private String notas;

    public ClienteDTO(Cliente entity) {
        this.id = entity.getId();
        this.nombre = entity.getNombre();
        this.email = entity.getEmail();
        this.telefono = entity.getTelefono();
        this.fechaAlta = entity.getFechaAlta();
        this.notas = entity.getNotas();
    }

    public Cliente toEntity() {
        Cliente entity = new Cliente();
        entity.setId(this.id);
        entity.setNombre(this.nombre);
        entity.setEmail(this.email);
        entity.setTelefono(this.telefono);
        entity.setFechaAlta(this.fechaAlta);
        entity.setNotas(this.notas);
        return entity;
    }
}