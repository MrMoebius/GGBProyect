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
 * Solo pide nombre y email. La contraseña se establece al verificar el email.
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

    /**
     * Convierte este DTO de registro a un ClienteDTO para reutilizar la lógica de creación.
     * No incluye password porque se establece después, al verificar el email.
     */
    public ClienteDTO toClienteDTO() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre(this.nombre);
        dto.setEmail(this.email);
        return dto;
    }
}
