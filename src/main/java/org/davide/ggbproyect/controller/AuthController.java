package org.davide.ggbproyect.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ClienteDTO;
import org.davide.ggbproyect.models.LoginDto;
import org.davide.ggbproyect.models.RegistroDTO;
import org.davide.ggbproyect.models.VerificacionDTO;
import org.davide.ggbproyect.repository.ClienteRepository;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.davide.ggbproyect.security.JwtTokenProvider;
import org.davide.ggbproyect.security.LoginRateLimiter;
import org.davide.ggbproyect.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginRateLimiter loginRateLimiter;
    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          LoginRateLimiter loginRateLimiter,
                          ClienteService clienteService,
                          ClienteRepository clienteRepository,
                          EmpleadoRepository empleadoRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginRateLimiter = loginRateLimiter;
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * Endpoint público de registro para clientes desde el frontend de Angular.
     * Cualquier persona puede crear una cuenta sin necesitar autenticación.
     * Se envía un email de verificación tras el registro.
     */
    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registro(@Valid @RequestBody RegistroDTO registroDTO) {
        // Reutilizamos la lógica de creación de ClienteService (validación, token, email)
        ClienteDTO clienteDTO = registroDTO.toClienteDTO();
        clienteService.create(clienteDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cuenta creada correctamente. Se ha enviado un email de verificación a " + registroDTO.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDto loginDto,
                                                      HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String key = loginDto.getEmail() + ":" + ip;

        if (loginRateLimiter.isBlocked(key) || loginRateLimiter.isBlocked(ip)) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Demasiados intentos fallidos. Cuenta bloqueada temporalmente.");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            loginRateLimiter.registerSuccessfulLogin(key);
            loginRateLimiter.registerSuccessfulLogin(ip);

            String token = jwtTokenProvider.generateToken(authentication);

            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(item -> item.getAuthority())
                    .orElse("ROLE_CLIENTE");

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", token);
            response.put("tokenType", "Bearer");
            response.put("role", role);
            response.put("email", authentication.getName());

            // Incluir id y nombre del cliente si existe
            clienteRepository.findByEmail(authentication.getName())
                    .ifPresent(cliente -> {
                        response.put("nombre", cliente.getNombre());
                        response.put("clienteId", cliente.getId());
                    });

            return ResponseEntity.ok(response);
        } catch (DisabledException e) {
            Map<String, Object> error = new HashMap<>();
            boolean isEmpleado = empleadoRepository.findByEmail(loginDto.getEmail()).isPresent();
            if (isEmpleado) {
                error.put("message", "Tu cuenta de empleado no esta activa. Contacta con un administrador.");
            } else {
                error.put("message", "Debe verificar su email antes de iniciar sesion. Revise su correo o solicite un nuevo enlace de verificacion.");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        } catch (BadCredentialsException e) {
            loginRateLimiter.registerFailedAttempt(key);
            loginRateLimiter.registerFailedAttempt(ip);
            throw e;
        }
    }

    /**
     * Endpoint para verificar el email y establecer la contraseña del cliente.
     * El frontend recoge el token de la URL y pide al usuario que elija su contraseña.
     * Ahora es POST porque recibe token + password en el body.
     */
    @PostMapping("/verificar-email")
    public ResponseEntity<Map<String, String>> verificarEmail(@Valid @RequestBody VerificacionDTO verificacionDTO) {
        clienteService.verificarEmail(verificacionDTO.getToken(), verificacionDTO.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email verificado y contraseña establecida correctamente. Ya puede iniciar sesión.");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para reenviar el email de verificación.
     * Útil si el primer email no llegó o el token expiró.
     * Es público porque el cliente no verificado no puede hacer login.
     */
    @PostMapping("/reenviar-verificacion")
    public ResponseEntity<Map<String, String>> reenviarVerificacion(@RequestParam String email) {
        clienteService.reenviarVerificacion(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Se ha enviado un nuevo email de verificación.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/solicitar-recuperacion")
    public ResponseEntity<Map<String, String>> solicitarRecuperacion(@RequestParam String email) {
        clienteService.solicitarRecuperacion(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Si el email existe, recibirás un enlace para restablecer tu contraseña.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<Map<String, String>> recuperarPassword(@Valid @RequestBody VerificacionDTO verificacionDTO) {
        clienteService.recuperarPassword(verificacionDTO.getToken(), verificacionDTO.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Contraseña restablecida correctamente. Ya puedes iniciar sesión.");
        return ResponseEntity.ok(response);
    }
}
