package org.davide.ggbproyect.service;

import lombok.RequiredArgsConstructor;
import org.davide.ggbproyect.models.Cliente;
import org.davide.ggbproyect.models.ClienteDTO;
import org.davide.ggbproyect.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // Horas de expiración del token de verificación (por defecto 24h)
    @Value("${app.email-verification.expiration-hours:24}")
    private int tokenExpirationHours;

    @Transactional(readOnly = true)
    public Page<ClienteDTO> getAll(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(ClienteDTO::new);
    }

    @Transactional(readOnly = true)
    public ClienteDTO getById(Integer id) {
        return clienteRepository.findById(id)
                .map(ClienteDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Cliente con id " + id + " no encontrado"));
    }

    public ClienteDTO create(ClienteDTO clienteDTO) {
        clienteRepository.findByEmail(clienteDTO.getEmail())
                .ifPresent(c -> {
                    throw new IllegalStateException("Ya existe un cliente con el email: " + clienteDTO.getEmail());
                });
        Cliente cliente = clienteDTO.toEntity();
        if (clienteDTO.getPassword() != null) {
            cliente.setPassword(passwordEncoder.encode(clienteDTO.getPassword()));
        }

        // Configurar verificación de email: generar token UUID y fecha de expiración
        cliente.setEmailVerificado(false);
        String token = UUID.randomUUID().toString();
        cliente.setTokenVerificacion(token);
        cliente.setTokenVerificacionExpira(LocalDateTime.now().plusHours(tokenExpirationHours));

        Cliente savedCliente = clienteRepository.save(cliente);

        // Enviar email de verificación (si falla el SMTP, la cuenta se crea igualmente)
        try {
            emailService.enviarEmailVerificacion(
                    savedCliente.getEmail(),
                    savedCliente.getNombre(),
                    token
            );
        } catch (Exception e) {
            log.warn("No se pudo enviar el email de verificación a {}. El cliente puede solicitar reenvío.",
                    savedCliente.getEmail(), e);
        }

        return new ClienteDTO(savedCliente);
    }

    public ClienteDTO update(Integer id, ClienteDTO clienteDTO) {
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente con id " + id + " no encontrado"));
        clienteRepository.findByEmail(clienteDTO.getEmail())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new IllegalStateException("Ya existe un cliente con el email: " + clienteDTO.getEmail());
                });

        // Comprobar si el email ha cambiado para resetear la verificación
        boolean emailCambiado = !existingCliente.getEmail().equals(clienteDTO.getEmail());

        existingCliente.setNombre(clienteDTO.getNombre());
        existingCliente.setEmail(clienteDTO.getEmail());
        existingCliente.setTelefono(clienteDTO.getTelefono());
        existingCliente.setFechaAlta(clienteDTO.getFechaAlta());
        existingCliente.setNotas(clienteDTO.getNotas());
        if (clienteDTO.getPassword() != null && !clienteDTO.getPassword().isBlank()) {
            existingCliente.setPassword(passwordEncoder.encode(clienteDTO.getPassword()));
        }

        // Si el email cambió, resetear verificación y enviar nuevo email
        if (emailCambiado) {
            existingCliente.setEmailVerificado(false);
            String token = UUID.randomUUID().toString();
            existingCliente.setTokenVerificacion(token);
            existingCliente.setTokenVerificacionExpira(LocalDateTime.now().plusHours(tokenExpirationHours));

            Cliente savedCliente = clienteRepository.save(existingCliente);

            try {
                emailService.enviarEmailVerificacion(
                        savedCliente.getEmail(),
                        savedCliente.getNombre(),
                        token
                );
            } catch (Exception e) {
                log.warn("No se pudo enviar el email de verificación tras cambio de email a {}.",
                        savedCliente.getEmail(), e);
            }

            return new ClienteDTO(savedCliente);
        }

        return new ClienteDTO(clienteRepository.save(existingCliente));
    }

    @Transactional(readOnly = true)
    public Page<ClienteDTO> filter(String nombre, String email, String telefono, Pageable pageable) {
        return clienteRepository.filter(nombre, email, telefono, pageable)
                .map(ClienteDTO::new);
    }

    public void changePassword(Integer id, String currentPassword, String newPassword) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente con id " + id + " no encontrado"));
        if (!passwordEncoder.matches(currentPassword, cliente.getPassword())) {
            throw new IllegalArgumentException("La contrasena actual es incorrecta");
        }
        cliente.setPassword(passwordEncoder.encode(newPassword));
        clienteRepository.save(cliente);
    }

    /**
     * Verifica el email de un cliente usando el token recibido por correo.
     * Además establece la contraseña que el cliente elige en ese momento.
     * Comprueba que el token sea válido y no haya expirado.
     */
    public void verificarEmail(String token, String password) {
        Cliente cliente = clienteRepository.findByTokenVerificacion(token)
                .orElseThrow(() -> new IllegalArgumentException("Token de verificación inválido"));

        if (Boolean.TRUE.equals(cliente.getEmailVerificado())) {
            throw new IllegalStateException("El email ya ha sido verificado");
        }

        // Comprobamos si el token ha expirado
        if (cliente.getTokenVerificacionExpira() == null || cliente.getTokenVerificacionExpira().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("El token de verificación ha expirado. Solicita uno nuevo.");
        }

        // Marcar como verificado, establecer contraseña y limpiar el token usado
        cliente.setEmailVerificado(true);
        cliente.setPassword(passwordEncoder.encode(password));
        cliente.setTokenVerificacion(null);
        cliente.setTokenVerificacionExpira(null);
        clienteRepository.save(cliente);
    }

    /**
     * Reenvía el email de verificación generando un nuevo token.
     * Útil si el cliente no recibió el primer email o el token expiró.
     */
    public void reenviarVerificacion(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró un cliente con el email: " + email));

        if (Boolean.TRUE.equals(cliente.getEmailVerificado())) {
            throw new IllegalStateException("El email ya ha sido verificado");
        }

        // Generar nuevo token y actualizar fecha de expiración
        String token = UUID.randomUUID().toString();
        cliente.setTokenVerificacion(token);
        cliente.setTokenVerificacionExpira(LocalDateTime.now().plusHours(tokenExpirationHours));
        clienteRepository.save(cliente);

        emailService.enviarEmailVerificacion(cliente.getEmail(), cliente.getNombre(), token);
    }

    public void solicitarRecuperacion(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró un cliente con el email: " + email));

        if (!Boolean.TRUE.equals(cliente.getEmailVerificado())) {
            throw new IllegalStateException("El email no ha sido verificado. Debes verificar tu cuenta primero.");
        }

        String token = UUID.randomUUID().toString();
        cliente.setTokenRecuperacion(token);
        cliente.setTokenRecuperacionExpira(LocalDateTime.now().plusHours(1));
        clienteRepository.save(cliente);

        emailService.enviarEmailRecuperacion(cliente.getEmail(), cliente.getNombre(), token);
    }

    public void recuperarPassword(String token, String newPassword) {
        Cliente cliente = clienteRepository.findByTokenRecuperacion(token)
                .orElseThrow(() -> new IllegalArgumentException("Token de recuperación inválido"));

        if (cliente.getTokenRecuperacionExpira() == null || cliente.getTokenRecuperacionExpira().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("El token de recuperación ha expirado. Solicita uno nuevo.");
        }

        cliente.setPassword(passwordEncoder.encode(newPassword));
        cliente.setTokenRecuperacion(null);
        cliente.setTokenRecuperacionExpira(null);
        clienteRepository.save(cliente);
    }

    public void delete(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente con id " + id + " no encontrado");
        }
        clienteRepository.deleteById(id);
    }
}
