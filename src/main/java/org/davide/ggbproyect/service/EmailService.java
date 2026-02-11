package org.davide.ggbproyect.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio para el envío de emails.
 * Utiliza Gmail SMTP configurado en application.properties.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    // Email remitente (el mismo configurado en spring.mail.username)
    @Value("${spring.mail.username}")
    private String fromEmail;

    // URL base para construir el enlace de verificación
    @Value("${app.email-verification.base-url}")
    private String baseUrl;

    /**
     * Envía un email de verificación al cliente recién creado.
     * El email contiene un enlace con el token para confirmar la cuenta.
     *
     * @param destinatario email del cliente
     * @param nombre       nombre del cliente (para personalizar el saludo)
     * @param token        token UUID de verificación
     */
    public void enviarEmailVerificacion(String destinatario, String nombre, String token) {
        // Construir el enlace de verificación con el token
        String enlace = baseUrl + "/api/auth/verificar-email?token=" + token;
        String asunto = "Verifica tu cuenta - GGBProyect";

        // Plantilla HTML del email de verificación (me lo ha hecho chati)
        String contenido = """
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <h2>Hola %s,</h2>
                <p>Gracias por registrarte en GGBProyect. Para completar tu registro, por favor verifica tu email haciendo clic en el siguiente enlace:</p>
                <p style="text-align: center; margin: 30px 0;">
                    <a href="%s" style="background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; font-size: 16px;">
                        Verificar mi cuenta
                    </a>
                </p>
                <p>O copia y pega este enlace en tu navegador:</p>
                <p><a href="%s">%s</a></p>
                <p><strong>Este enlace expirará en 24 horas.</strong></p>
                <p>Si no has solicitado esta cuenta, puedes ignorar este email.</p>
                <br>
                <p>Saludos,<br>Equipo GGBProyect</p>
                </body>
                </html>
                """.formatted(nombre, enlace, enlace, enlace);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            // true = contenido HTML
            helper.setText(contenido, true);
            mailSender.send(message);
            log.info("Email de verificación enviado correctamente a: {}", destinatario);
        } catch (MessagingException e) {
            log.error("Error enviando email de verificación a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error al enviar el email de verificación", e);
        }
    }
}
