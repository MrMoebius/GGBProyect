package org.davide.ggbproyect.controller;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/eventos")
public class EventoImagenController {

    private static final Logger log = LoggerFactory.getLogger(EventoImagenController.class);
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final Map<String, String> TYPE_TO_EXT = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );

    private final Path uploadDir;

    public EventoImagenController(@Value("${app.upload.events-dir}") String uploadPath) {
        this.uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(uploadDir);
        log.info("Directorio de imagenes de eventos: {}", uploadDir);
    }

    @PostMapping("/{id}/imagen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadImagen(@PathVariable Integer id,
                                          @RequestParam("file") MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Tipo de archivo no permitido",
                    "message", "Solo se permiten imagenes JPEG, PNG o WebP"
            ));
        }

        deleteExistingImage(id);

        String ext = TYPE_TO_EXT.get(contentType);
        Path target = uploadDir.resolve(id + ext);
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        }

        log.info("Imagen subida para evento {}: {}", id, target.getFileName());
        return ResponseEntity.ok(Map.of("message", "Imagen subida correctamente"));
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
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(resource);
    }

    @DeleteMapping("/{id}/imagen")
    @PreAuthorize("hasRole('ADMIN')")
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
