package org.davide.ggbproyect.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TranslationService {

    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);
    private static final String MYMEMORY_API_URL = "https://api.mymemory.translated.net/get";
    private static final int MAX_CHUNK_SIZE = 500;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public TranslationService() {
        this.restClient = RestClient.create();
        this.objectMapper = new ObjectMapper();
    }

    public String translate(String text, String targetLang) {
        if (text == null || text.isBlank()) {
            return text;
        }

        String langPair = "en|" + targetLang.toLowerCase();

        if (text.length() <= MAX_CHUNK_SIZE) {
            return translateChunk(text, langPair);
        }

        StringBuilder result = new StringBuilder();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + MAX_CHUNK_SIZE, text.length());
            if (end < text.length()) {
                int lastDot = text.lastIndexOf(". ", end);
                if (lastDot > start) {
                    end = lastDot + 2;
                }
            }
            String chunk = text.substring(start, end);
            result.append(translateChunk(chunk, langPair));
            start = end;
        }
        return result.toString();
    }

    private String translateChunk(String text, String langPair) {
        try {
            String encoded = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String url = MYMEMORY_API_URL + "?q=" + encoded + "&langpair=" + langPair;

            String response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode responseData = root.get("responseData");
            if (responseData != null) {
                String translated = responseData.get("translatedText").asText();
                if (translated != null && !translated.isBlank()) {
                    return translated;
                }
            }
            return text;
        } catch (Exception e) {
            log.warn("Error al traducir con MyMemory: {}", e.getMessage());
            return text;
        }
    }
}
