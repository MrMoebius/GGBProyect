package org.davide.ggbproyect.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO específico para el registro público de clientes desde el frontend.
 * Solo contiene los campos obligatorios para crear una cuenta.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {

    @NotNull
    @NotBlank
    @Size(max = 150)
    private String nombre;

    @NotNull
    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "El email debe tener un formato válido")
    @Size(max = 150)
    private String email;

    // La contraseña es obligatoria en el registro (mínimo 6 caracteres)
    @NotNull
    @NotBlank
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    /**
     * Convierte este DTO de registro a un ClienteDTO para reutilizar la lógica de creación.
     */
    public ClienteDTO toClienteDTO() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre(this.nombre);
        dto.setEmail(this.email);
        dto.setPassword(this.password);
        return dto;
    }
}
