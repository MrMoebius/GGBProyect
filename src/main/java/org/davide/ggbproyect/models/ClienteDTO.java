package org.davide.ggbproyect.models;

import jakarta.validation.constraints.Email;
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

    private Integer id;

    @NotNull
    @Size(max = 150)
    private String nombre;

    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String telefono;

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