package org.davide.ggbproyect.controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.davide.ggbproyect.models.ChangePasswordDTO;
import org.davide.ggbproyect.models.ClienteDTO;
import org.davide.ggbproyect.models.Cliente;
import org.davide.ggbproyect.repository.ClienteRepository;
import org.davide.ggbproyect.service.ClienteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final Map<String, String> TYPE_TO_EXT = Map.of(
            "image/jpeg", ".jpg", "image/png", ".png", "image/webp", ".webp");

    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;
    private final Path uploadDir;

    public ClienteController(ClienteService clienteService,
                             ClienteRepository clienteRepository,
                             @Value("${app.upload.clientes-dir}") String uploadPath) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
        this.uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(uploadDir);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Page<ClienteDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(clienteService.getAll(pageable));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Page<ClienteDTO>> filter(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefono,
            Pageable pageable) {
        return ResponseEntity.ok(clienteService.filter(nombre, email, telefono, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<ClienteDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO created = clienteService.create(clienteDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> update(@PathVariable Integer id, @Valid @RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.ok(clienteService.update(id, clienteDTO));
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<Void> changePassword(@PathVariable Integer id,
                                                @Valid @RequestBody ChangePasswordDTO dto,
                                                org.springframework.security.core.Authentication authentication) {
        String currentUserEmail = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isEmpleado = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLEADO"));

        if (!isAdmin && !isEmpleado) {
            ClienteDTO cliente = clienteService.getById(id);
            if (!cliente.getEmail().equals(currentUserEmail)) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "No tiene permisos para cambiar la contraseña de otro usuario");
            }
        }

        clienteService.changePassword(id, dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ===== Foto de perfil =====

    @PostMapping("/me/imagen")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> uploadFotoPerfil(@RequestParam("file") MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Solo se permiten imagenes JPEG, PNG o WebP"));
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Integer id = cliente.getId();
        deleteExistingImage(id);

        String ext = TYPE_TO_EXT.get(contentType);
        Path target = uploadDir.resolve(id + ext);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok(Map.of("message", "Foto de perfil actualizada"));
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<Resource> getFotoPerfil(@PathVariable Integer id) throws IOException {
        Path imagePath = findImageFile(id);
        if (imagePath == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(imagePath.toUri());
        String contentType = Files.probeContentType(imagePath);
        if (contentType == null) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(resource);
    }

    @DeleteMapping("/me/imagen")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> deleteFotoPerfil() throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        boolean deleted = deleteExistingImage(cliente.getId());
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Foto eliminada"));
        }
        return ResponseEntity.notFound().build();
    }

    private Path findImageFile(Integer id) {
        for (String ext : List.of(".jpg", ".png", ".webp")) {
            Path candidate = uploadDir.resolve(id + ext);
            if (Files.exists(candidate)) return candidate;
        }
        return null;
    }

    private boolean deleteExistingImage(Integer id) throws IOException {
        boolean deleted = false;
        for (String ext : List.of(".jpg", ".png", ".webp")) {
            if (Files.deleteIfExists(uploadDir.resolve(id + ext))) deleted = true;
        }
        return deleted;
    }
}
