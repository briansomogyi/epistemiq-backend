package com.example.epistemiq_backend.gemini;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.genai.Client;
import com.google.genai.types.ContentEmbedding;
import com.google.genai.types.EmbedContentResponse;
import com.google.genai.types.GenerateContentResponse;

@Service
public class GeminiService {

    @Value("${app.gemini.api-key}")
    private String apiKey;

    @Value("${app.gemini.chat-model}")
    private String chatModel;

    @Value("${app.gemini.embedding-model}")
    private String embeddingModel;

    public String chatWithGemini(String prompt) {
        try (Client client = Client.builder().apiKey(apiKey).build()) {

            GenerateContentResponse response = client.models.generateContent(
                    chatModel, // Use injected variable
                    prompt,
                    null);

            return response.text();

        } catch (Exception e) {
            // Throw an HTTP 500 error instead of returning a success string
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to generate response from Gemini", e);
        }
    }

    public List<Float> generateEmbedding(String text) {
        try (Client client = Client.builder().apiKey(apiKey).build()) {

            EmbedContentResponse response = client.models.embedContent(
                    embeddingModel, // Use injected variable
                    text,
                    null);

            if (response.embeddings().isPresent() && !response.embeddings().get().isEmpty()) {
                ContentEmbedding embedding = response.embeddings().get().get(0);
                if (embedding.values().isPresent()) {
                    return embedding.values().get();
                }
            }
            return List.of();

        } catch (Exception e) {
            // Throw an HTTP 500 error
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to generate Gemini embedding", e);
        }
    }
}
