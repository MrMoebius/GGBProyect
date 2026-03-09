package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.BggGameDetailsDTO;
import org.davide.ggbproyect.models.BggSearchResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BggService {

    private static final Logger log = LoggerFactory.getLogger(BggService.class);
    private static final String BGG_SEARCH_URL = "https://boardgamegeek.com/xmlapi2/search";
    private static final String BGG_THING_URL = "https://boardgamegeek.com/xmlapi2/thing";
    private static final int MAX_RESULTS = 20;
    private static final long MIN_REQUEST_INTERVAL_MS = 5000;

    private final RestClient restClient;
    private final RestClient imageClient;
    private final TranslationService translationService;
    private final WikidataService wikidataService;
    private final Path gamesUploadDir;
    private long lastRequestTime = 0;
    private volatile boolean bggAvailable = true;

    private final Map<Integer, String> imageUrlCache = new ConcurrentHashMap<>();

    private static final Map<String, String> CATEGORY_MAP = Map.ofEntries(
            Map.entry("Strategy", "ESTRATEGIA"),
            Map.entry("Abstract Strategy", "ESTRATEGIA"),
            Map.entry("Family Game", "FAMILIAR"),
            Map.entry("Card Game", "CARTAS"),
            Map.entry("Party Game", "PARTY"),
            Map.entry("Adventure", "AVENTURA"),
            Map.entry("Action / Dexterity", "ACCION"),
            Map.entry("Children's Game", "INFANTIL"),
            Map.entry("Puzzle", "PUZZLE"),
            Map.entry("Horror", "TERROR"),
            Map.entry("Murder/Mystery", "MISTERIO"),
            Map.entry("Racing", "CARRERAS"),
            Map.entry("Dice", "DADOS"),
            Map.entry("Miniatures", "MINIATURAS"),
            Map.entry("Fantasy", "ROL"),
            Map.entry("Wargame", "ROL"),
            Map.entry("Exploration", "AVENTURA"),
            Map.entry("Fighting", "ACCION"),
            Map.entry("Deduction", "MISTERIO"),
            Map.entry("Economic", "ESTRATEGIA"),
            Map.entry("Negotiation", "ESTRATEGIA")
    );

    public BggService(TranslationService translationService,
                      WikidataService wikidataService,
                      @Value("${app.upload.games-dir}") String uploadPath,
                      @Value("${bgg.api-token:}") String bggApiToken) {
        this.translationService = translationService;
        this.wikidataService = wikidataService;
        RestClient.Builder builder = RestClient.builder()
                .defaultHeader("User-Agent", "GGBar/1.0");
        if (bggApiToken != null && !bggApiToken.isBlank()) {
            builder.defaultHeader("Authorization", "Bearer " + bggApiToken);
        }
        this.restClient = builder.build();
        this.imageClient = RestClient.builder()
                .defaultHeader("User-Agent", "GGBar/1.0")
                .build();
        this.gamesUploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
    }

    public List<BggSearchResultDTO> search(String query) {
        if (bggAvailable) {
            try {
                return searchBgg(query);
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("401")) {
                    log.warn("BGG no disponible (401), usando Wikidata como fallback");
                    bggAvailable = false;
                } else {
                    log.error("Error buscando en BGG: {}", e.getMessage());
                }
            }
        }
        log.info("Buscando en Wikidata (fallback): '{}'", query);
        return wikidataService.search(query);
    }

    public BggGameDetailsDTO getDetails(Integer id) {
        if (bggAvailable) {
            try {
                BggGameDetailsDTO details = getDetailsBgg(id);
                cacheImageUrl(id, details.getImageUrl());
                return details;
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("401")) {
                    log.warn("BGG no disponible (401), usando Wikidata como fallback");
                    bggAvailable = false;
                } else {
                    log.error("Error obteniendo detalles de BGG: {}", e.getMessage());
                }
            }
        }
        log.info("Obteniendo detalles de Wikidata (fallback) para id {}", id);
        BggGameDetailsDTO details = wikidataService.getDetails(id);
        cacheImageUrl(id, details.getImageUrl());
        return details;
    }

    public void downloadImage(Integer sourceId, Integer juegoId) {
        try {
            String imageUrl = imageUrlCache.get(sourceId);
            if (imageUrl == null) {
                BggGameDetailsDTO details = getDetails(sourceId);
                imageUrl = details.getImageUrl();
            }

            if (imageUrl == null || imageUrl.isBlank()) {
                log.warn("No hay imagen disponible para id {}", sourceId);
                return;
            }

            byte[] imageBytes = imageClient.get()
                    .uri(imageUrl)
                    .retrieve()
                    .body(byte[].class);

            if (imageBytes == null || imageBytes.length == 0) {
                log.warn("Imagen vacia descargada para id {}", sourceId);
                return;
            }

            String ext = guessExtension(imageUrl);
            deleteExistingImages(juegoId);
            Files.createDirectories(gamesUploadDir);
            Path target = gamesUploadDir.resolve(juegoId + ext);
            Files.write(target, imageBytes);

            log.info("Imagen descargada para juego {}: {}", juegoId, target.getFileName());
        } catch (Exception e) {
            log.error("Error descargando imagen para juego {}: {}", juegoId, e.getMessage());
            throw new RuntimeException("Error al descargar imagen: " + e.getMessage());
        }
    }

    private void cacheImageUrl(Integer id, String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank()) {
            imageUrlCache.put(id, imageUrl);
        }
    }

    private List<BggSearchResultDTO> searchBgg(String query) throws Exception {
        enforceRateLimit();
        String url = BGG_SEARCH_URL + "?query=" + java.net.URLEncoder.encode(query, "UTF-8") + "&type=boardgame";
        String xml = restClient.get().uri(url).retrieve().body(String.class);
        Document doc = parseXml(xml);

        NodeList items = doc.getElementsByTagName("item");
        List<BggSearchResultDTO> results = new ArrayList<>();

        for (int i = 0; i < items.getLength() && results.size() < MAX_RESULTS; i++) {
            Element item = (Element) items.item(i);
            Integer bggId = Integer.parseInt(item.getAttribute("id"));
            String name = getNameValue(item);
            Integer year = getIntAttribute(item, "yearpublished");
            results.add(new BggSearchResultDTO(bggId, name, year));
        }
        return results;
    }

    private BggGameDetailsDTO getDetailsBgg(Integer bggId) throws Exception {
        enforceRateLimit();
        String url = BGG_THING_URL + "?id=" + bggId + "&stats=1";
        String xml = restClient.get().uri(url).retrieve().body(String.class);
        Document doc = parseXml(xml);

        NodeList items = doc.getElementsByTagName("item");
        if (items.getLength() == 0) {
            throw new jakarta.persistence.EntityNotFoundException("Juego no encontrado en BGG con id: " + bggId);
        }

        Element item = (Element) items.item(0);

        String nombre = getPrimaryName(item);
        Integer minPlayers = getIntAttribute(item, "minplayers");
        Integer maxPlayers = getIntAttribute(item, "maxplayers");
        Integer playingTime = getIntAttribute(item, "playingtime");
        String imageUrl = getTextContent(item, "image");
        String rawDescription = getTextContent(item, "description");
        Double weight = getAverageWeight(item);
        Integer yearPublished = getIntAttribute(item, "yearpublished");

        String descripcion = decodeHtmlEntities(rawDescription);
        if (descripcion != null && descripcion.length() > 2000) {
            descripcion = descripcion.substring(0, 2000) + "...";
        }
        descripcion = translationService.translate(descripcion, "ES");

        String complejidad = mapComplejidad(weight);
        String genero = mapCategories(item);

        return new BggGameDetailsDTO(
                bggId, nombre, minPlayers, maxPlayers, playingTime,
                complejidad, genero, descripcion, imageUrl, weight, yearPublished
        );
    }

    private synchronized void enforceRateLimit() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRequestTime;
        if (elapsed < MIN_REQUEST_INTERVAL_MS) {
            try {
                Thread.sleep(MIN_REQUEST_INTERVAL_MS - elapsed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        lastRequestTime = System.currentTimeMillis();
    }

    private Document parseXml(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
    }

    private String getPrimaryName(Element item) {
        NodeList names = item.getElementsByTagName("name");
        for (int i = 0; i < names.getLength(); i++) {
            Element nameEl = (Element) names.item(i);
            if ("primary".equals(nameEl.getAttribute("type"))) {
                return nameEl.getAttribute("value");
            }
        }
        if (names.getLength() > 0) {
            return ((Element) names.item(0)).getAttribute("value");
        }
        return null;
    }

    private String getNameValue(Element item) {
        NodeList names = item.getElementsByTagName("name");
        if (names.getLength() > 0) {
            return ((Element) names.item(0)).getAttribute("value");
        }
        return null;
    }

    private Integer getIntAttribute(Element item, String tagName) {
        NodeList list = item.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            String val = ((Element) list.item(0)).getAttribute("value");
            if (val != null && !val.isEmpty()) {
                try {
                    return Integer.parseInt(val);
                } catch (NumberFormatException ignored) {}
            }
        }
        return null;
    }

    private String getTextContent(Element item, String tagName) {
        NodeList list = item.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent();
        }
        return null;
    }

    private Double getAverageWeight(Element item) {
        NodeList stats = item.getElementsByTagName("statistics");
        if (stats.getLength() > 0) {
            Element statsEl = (Element) stats.item(0);
            NodeList ratings = statsEl.getElementsByTagName("ratings");
            if (ratings.getLength() > 0) {
                Element ratingsEl = (Element) ratings.item(0);
                NodeList weights = ratingsEl.getElementsByTagName("averageweight");
                if (weights.getLength() > 0) {
                    String val = ((Element) weights.item(0)).getAttribute("value");
                    try {
                        return Double.parseDouble(val);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return null;
    }

    private String mapComplejidad(Double weight) {
        if (weight == null) return null;
        if (weight <= 2.0) return "BAJA";
        if (weight <= 3.5) return "MEDIA";
        return "ALTA";
    }

    private String mapCategories(Element item) {
        NodeList links = item.getElementsByTagName("link");
        Set<String> mapped = new LinkedHashSet<>();
        for (int i = 0; i < links.getLength(); i++) {
            Element link = (Element) links.item(i);
            if ("boardgamecategory".equals(link.getAttribute("type"))) {
                String bggCategory = link.getAttribute("value");
                String genre = CATEGORY_MAP.get(bggCategory);
                if (genre != null) {
                    mapped.add(genre);
                }
            }
        }
        return mapped.isEmpty() ? null : String.join(", ", mapped);
    }

    private String decodeHtmlEntities(String text) {
        if (text == null) return null;
        return text
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#10;", "\n")
                .replace("&apos;", "'")
                .replace("&mdash;", "\u2014")
                .replace("&ndash;", "\u2013")
                .replaceAll("<[^>]+>", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String guessExtension(String url) {
        if (url == null) return ".jpg";
        String lower = url.toLowerCase();
        if (lower.contains(".png")) return ".png";
        if (lower.contains(".webp")) return ".webp";
        return ".jpg";
    }

    private void deleteExistingImages(Integer juegoId) {
        for (String ext : List.of(".jpg", ".png", ".webp")) {
            Path candidate = gamesUploadDir.resolve(juegoId + ext);
            try {
                Files.deleteIfExists(candidate);
            } catch (Exception ignored) {}
        }
    }
}
