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

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.email-verification.base-url}")
    private String baseUrl;

    /**
     * Envía una factura por email al cliente en formato ticket.
     */
    public void enviarFactura(String destinatario, String nombreCliente, String numeroFactura,
                              String fechaEmision, String baseIva10, String cuotaIva10,
                              String baseIva21, String cuotaIva21, String importeLudoteca,
                              String total, String totalPagado, String estado) {
        String asunto = "Factura " + numeroFactura + " - GG Bar";
        String contenido = """
                <html>
                <body style="font-family: 'Courier New', monospace; max-width: 400px; margin: 0 auto; padding: 20px; color: #333; background: #fff;">
                <div style="text-align: center; margin-bottom: 16px;">
                    <h2 style="margin: 0; font-size: 20px;">GG Bar</h2>
                    <p style="margin: 4px 0; font-size: 13px; color: #666;">Factura Simplificada</p>
                    <p style="margin: 4px 0; font-weight: bold; font-size: 14px;">%s</p>
                    <p style="margin: 4px 0; font-size: 12px;">%s</p>
                </div>
                <hr style="border: none; border-top: 1px dashed #999;">
                <p style="font-size: 13px;">Cliente: %s</p>
                <hr style="border: none; border-top: 1px dashed #999;">
                <table style="width: 100%%; font-size: 12px; border-collapse: collapse;">
                    <tr><td>Base IVA 10%%</td><td style="text-align: right;">%s EUR</td></tr>
                    <tr><td>Cuota IVA 10%%</td><td style="text-align: right;">%s EUR</td></tr>
                    <tr><td>Base IVA 21%%</td><td style="text-align: right;">%s EUR</td></tr>
                    <tr><td>Cuota IVA 21%%</td><td style="text-align: right;">%s EUR</td></tr>
                    %s
                </table>
                <hr style="border: none; border-top: 1px dashed #999;">
                <table style="width: 100%%; font-size: 14px; font-weight: bold; border-collapse: collapse;">
                    <tr><td>TOTAL</td><td style="text-align: right;">%s EUR</td></tr>
                    <tr><td style="font-weight: normal; font-size: 12px;">Pagado</td><td style="text-align: right; font-weight: normal; font-size: 12px;">%s EUR</td></tr>
                </table>
                <hr style="border: none; border-top: 1px dashed #999;">
                <p style="text-align: center; font-size: 11px; color: #666;">Estado: %s</p>
                <p style="text-align: center; font-size: 11px; color: #666;">Gracias por su visita · IVA incluido</p>
                </body>
                </html>
                """.formatted(
                numeroFactura, fechaEmision, nombreCliente,
                baseIva10, cuotaIva10, baseIva21, cuotaIva21,
                (importeLudoteca != null && !importeLudoteca.equals("0,00"))
                        ? "<tr><td>Ludoteca</td><td style=\"text-align: right;\">" + importeLudoteca + " EUR</td></tr>"
                        : "",
                total, totalPagado, estado
        );

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenido, true);
            mailSender.send(message);
            log.info("Factura {} enviada por email a: {}", numeroFactura, destinatario);
        } catch (MessagingException e) {
            log.error("Error enviando factura por email a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error al enviar la factura por email", e);
        }
    }

    /**
     * Envía un email de verificación al cliente recién creado.
     * El email contiene un enlace con el token para confirmar la cuenta.
     *
     * @param destinatario email del cliente
     * @param nombre       nombre del cliente (para personalizar el saludo)
     * @param token        token UUID de verificación
     */
    public void enviarEmailVerificacion(String destinatario, String nombre, String token) {
        String enlace = baseUrl + "/auth/verificar-email?token=" + token;
        String asunto = "Verifica tu cuenta - GGBProyect";

        // Plantilla HTML del email de verificación
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
            helper.setText(contenido, true);
            mailSender.send(message);
            log.info("Email de verificación enviado correctamente a: {}", destinatario);
        } catch (MessagingException e) {
            log.error("Error enviando email de verificación a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error al enviar el email de verificación", e);
        }
    }

    public void enviarEmailRecuperacion(String destinatario, String nombre, String token) {
        String enlace = baseUrl + "/auth/recuperar-password?token=" + token;
        String asunto = "Recuperar contraseña - GGBProyect";

        String contenido = """
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <h2>Hola %s,</h2>
                <p>Hemos recibido una solicitud para restablecer la contraseña de tu cuenta. Haz clic en el siguiente enlace para elegir una nueva contraseña:</p>
                <p style="text-align: center; margin: 30px 0;">
                    <a href="%s" style="background-color: #FF6B6B; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; font-size: 16px;">
                        Restablecer contraseña
                    </a>
                </p>
                <p>O copia y pega este enlace en tu navegador:</p>
                <p><a href="%s">%s</a></p>
                <p><strong>Este enlace expirará en 1 hora.</strong></p>
                <p>Si no has solicitado este cambio, puedes ignorar este email. Tu contraseña no se modificará.</p>
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
            helper.setText(contenido, true);
            mailSender.send(message);
            log.info("Email de recuperación enviado correctamente a: {}", destinatario);
        } catch (MessagingException e) {
            log.error("Error enviando email de recuperación a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error al enviar el email de recuperación", e);
        }
    }
}
