package org.davide.ggbproyect.controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.davide.ggbproyect.models.JuegoDTO;
import org.davide.ggbproyect.service.JuegoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/juegos")
public class JuegoController {

    private static final Logger log = LoggerFactory.getLogger(JuegoController.class);
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final Map<String, String> TYPE_TO_EXT = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );

    private final JuegoService juegoService;
    private final Path uploadDir;

    public JuegoController(JuegoService juegoService,
                           @Value("${app.upload.games-dir}") String uploadPath) {
        this.juegoService = juegoService;
        this.uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(uploadDir);
        log.info("Directorio de imagenes de juegos: {}", uploadDir);
    }

    @GetMapping
    public ResponseEntity<Page<JuegoDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(juegoService.getAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<JuegoDTO>> filter(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String complejidad,
            @RequestParam(required = false) String idioma,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) Boolean recomendadoDosJugadores,
            Pageable pageable) {
        return ResponseEntity.ok(juegoService.filter(nombre, complejidad, idioma, ubicacion, activo, recomendadoDosJugadores, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuegoDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(juegoService.getById(id));
    }

    @GetMapping("/exists")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Map<String, Boolean>> existsByNombre(@RequestParam String nombre) {
        boolean exists = juegoService.existsByNombre(nombre);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<JuegoDTO> create(@Valid @RequestBody JuegoDTO juegoDTO) {
        JuegoDTO created = juegoService.create(juegoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<JuegoDTO> update(@PathVariable Integer id, @Valid @RequestBody JuegoDTO juegoDTO) {
        return ResponseEntity.ok(juegoService.update(id, juegoDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        juegoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // === Imagen endpoints ===

    @PostMapping("/{id}/imagen")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<?> uploadImagen(@PathVariable Integer id,
                                          @RequestParam("file") MultipartFile file) throws IOException {
        // Verificar que el juego existe
        juegoService.getById(id);

        // Validar tipo
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Tipo de archivo no permitido",
                    "message", "Solo se permiten imagenes JPEG, PNG o WebP"
            ));
        }

        // Borrar imagen previa de este juego (puede tener otra extension)
        deleteExistingImage(id);

        // Guardar con la extension correcta
        String ext = TYPE_TO_EXT.get(contentType);
        Path target = uploadDir.resolve(id + ext);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        log.info("Imagen subida para juego {}: {}", id, target.getFileName());
        return ResponseEntity.ok(Map.of("message", "Imagen subida correctamente"));
    }

    @PostMapping("/{targetId}/copy-imagen/{sourceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<?> copyImagen(@PathVariable Integer targetId, @PathVariable Integer sourceId) throws IOException {
        juegoService.getById(targetId);
        Path sourceImage = findImageFile(sourceId);
        if (sourceImage == null) {
            return ResponseEntity.notFound().build();
        }
        deleteExistingImage(targetId);
        String fileName = sourceImage.getFileName().toString();
        String ext = fileName.substring(fileName.lastIndexOf('.'));
        Path target = uploadDir.resolve(targetId + ext);
        Files.copy(sourceImage, target, StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok(Map.of("message", "Imagen copiada correctamente"));
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<Resource> getImagen(@PathVariable Integer id) throws IOException {
        Path imagePath = findImageFile(id);
        if (imagePath == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(imagePath.toUri());
        String contentType = Files.probeContentType(imagePath);
        if (contentType == null) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                .body(resource);
    }

    @DeleteMapping("/{id}/imagen")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<?> deleteImagen(@PathVariable Integer id) throws IOException {
        boolean deleted = deleteExistingImage(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Imagen eliminada"));
        }
        return ResponseEntity.notFound().build();
    }

    private Path findImageFile(Integer id) {
        for (String ext : List.of(".jpg", ".png", ".webp")) {
            Path candidate = uploadDir.resolve(id + ext);
            if (Files.exists(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private boolean deleteExistingImage(Integer id) throws IOException {
        boolean deleted = false;
        for (String ext : List.of(".jpg", ".png", ".webp")) {
            Path candidate = uploadDir.resolve(id + ext);
            if (Files.deleteIfExists(candidate)) {
                deleted = true;
            }
        }
        return deleted;
    }
}
