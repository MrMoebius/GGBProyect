package org.davide.ggbproyect.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.davide.ggbproyect.models.BggGameDetailsDTO;
import org.davide.ggbproyect.models.BggSearchResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WikidataService {

    private static final Logger log = LoggerFactory.getLogger(WikidataService.class);
    private static final String SPARQL_ENDPOINT = "https://query.wikidata.org/sparql";
    private static final int MAX_RESULTS = 20;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public WikidataService() {
        this.restClient = RestClient.builder()
                .defaultHeader("User-Agent", "GGBar/1.0")
                .defaultHeader("Accept", "application/json")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public List<BggSearchResultDTO> search(String query) {
        try {
            String sparql = "SELECT DISTINCT ?game ?gameLabel ?yearPublished WHERE { "
                    + "?game wdt:P31 wd:Q131436. "
                    + "?game rdfs:label ?label. "
                    + "FILTER(CONTAINS(LCASE(?label), LCASE(\"" + escapeSparql(query) + "\"))). "
                    + "FILTER(LANG(?label) = \"en\" || LANG(?label) = \"es\"). "
                    + "OPTIONAL { ?game wdt:P577 ?pubDate } "
                    + "BIND(YEAR(?pubDate) AS ?yearPublished) "
                    + "SERVICE wikibase:label { bd:serviceParam wikibase:language \"es,en\" } "
                    + "} LIMIT " + MAX_RESULTS;

            String url = SPARQL_ENDPOINT + "?format=json&query=" + URLEncoder.encode(sparql, StandardCharsets.UTF_8);
            String response = restClient.get().uri(URI.create(url)).retrieve().body(String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode bindings = root.path("results").path("bindings");

            List<BggSearchResultDTO> results = new ArrayList<>();
            for (JsonNode binding : bindings) {
                Integer wikidataId = extractQNumber(binding.path("game").path("value").asText());
                String name = binding.path("gameLabel").path("value").asText();
                Integer year = binding.has("yearPublished") && !binding.path("yearPublished").path("value").asText().isEmpty()
                        ? Integer.parseInt(binding.path("yearPublished").path("value").asText())
                        : null;
                if (wikidataId != null) {
                    results.add(new BggSearchResultDTO(wikidataId, name, year));
                }
            }
            log.info("Wikidata: {} resultados para '{}'", results.size(), query);
            return results;
        } catch (Exception e) {
            log.error("Error buscando en Wikidata: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public BggGameDetailsDTO getDetails(Integer wikidataId) {
        try {
            String sparql = "SELECT ?gameLabel ?gameDescription ?image ?minPlayers ?maxPlayers ?duration ?yearPublished WHERE { "
                    + "BIND(wd:Q" + wikidataId + " AS ?game) "
                    + "OPTIONAL { ?game wdt:P18 ?image } "
                    + "OPTIONAL { ?game wdt:P1872 ?minPlayers } "
                    + "OPTIONAL { ?game wdt:P1873 ?maxPlayers } "
                    + "OPTIONAL { ?game wdt:P2047 ?duration } "
                    + "OPTIONAL { ?game wdt:P577 ?pubDate } "
                    + "BIND(YEAR(?pubDate) AS ?yearPublished) "
                    + "SERVICE wikibase:label { bd:serviceParam wikibase:language \"es,en\" } "
                    + "} LIMIT 1";

            String url = SPARQL_ENDPOINT + "?format=json&query=" + URLEncoder.encode(sparql, StandardCharsets.UTF_8);
            String response = restClient.get().uri(URI.create(url)).retrieve().body(String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode bindings = root.path("results").path("bindings");

            if (!bindings.isEmpty()) {
                JsonNode b = bindings.get(0);
                String nombre = getStringValue(b, "gameLabel");
                String descripcion = getStringValue(b, "gameDescription");
                String imageUrl = getStringValue(b, "image");
                Integer minPlayers = getIntValue(b, "minPlayers");
                Integer maxPlayers = getIntValue(b, "maxPlayers");
                Integer duration = getIntValue(b, "duration");
                Integer year = getIntValue(b, "yearPublished");

                return new BggGameDetailsDTO(
                        wikidataId, nombre, minPlayers, maxPlayers, duration,
                        null, null, descripcion, imageUrl, null, year
                );
            }
            throw new RuntimeException("No se encontraron datos en Wikidata para Q" + wikidataId);
        } catch (Exception e) {
            log.error("Error obteniendo detalles de Wikidata Q{}: {}", wikidataId, e.getMessage());
            throw new RuntimeException("Error al obtener detalles de Wikidata: " + e.getMessage());
        }
    }

    private Integer extractQNumber(String uri) {
        if (uri == null) return null;
        int idx = uri.lastIndexOf("/Q");
        if (idx >= 0) {
            try {
                return Integer.parseInt(uri.substring(idx + 2));
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    private String getStringValue(JsonNode binding, String field) {
        JsonNode node = binding.path(field);
        if (node.isMissingNode() || node.path("value").asText().isEmpty()) return null;
        return node.path("value").asText();
    }

    private Integer getIntValue(JsonNode binding, String field) {
        String val = getStringValue(binding, field);
        if (val == null) return null;
        try {
            return (int) Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String escapeSparql(String input) {
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
