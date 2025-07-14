package br.com.alura.literalura_api.service;

import br.com.alura.literalura_api.dto.GutendexResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

@Service
public class GutendexService {
    
    private static final String BASE_URL = "https://gutendex.com/books/";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public GutendexService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    public Optional<GutendexResponse> searchBooks(String title) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String url = BASE_URL + "?search=" + encodedTitle;
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                GutendexResponse gutendexResponse = objectMapper.readValue(
                        response.body(), GutendexResponse.class);
                return Optional.of(gutendexResponse);
            } else {
                System.err.println("API call failed with status: " + response.statusCode());
                return Optional.empty();
            }
            
        } catch (IOException | InterruptedException e) {
            System.err.println("Error calling Gutendex API: " + e.getMessage());
            return Optional.empty();
        }
    }
}