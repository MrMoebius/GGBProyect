package org.davide.ggbproyect.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la verificación de email.
 * El cliente recibe el token por correo y al hacer clic establece su contraseña.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificacionDTO {

    // Token UUID que se envió por email
    @NotNull
    @NotBlank
    private String token;

    // Contraseña que el cliente elige al verificar su cuenta
    @NotNull
    @NotBlank
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;
}
