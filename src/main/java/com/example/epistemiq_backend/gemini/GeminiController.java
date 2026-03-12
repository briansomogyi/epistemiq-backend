package com.example.epistemiq_backend.gemini;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gemini")
@CrossOrigin(origins = "*")
public class GeminiController {

    private final GeminiService geminiService;

    // Note: @Autowired is optional on a single constructor in modern Spring
    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // This maps to an HTTP POST request
    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> ask(@RequestBody ChatRequest request) {
        if (request.question() == null || request.question().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String answer = geminiService.chatWithGemini(request.question());
        return ResponseEntity.ok(new ChatResponse(answer));
    }

    // This maps to an HTTP POST request for generating embeddings
    @PostMapping("/embed")
    public ResponseEntity<List<Float>> getEmbedding(@RequestBody EmbedRequest request) {
        if (request.text() == null || request.text().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Float> embeddingVector = geminiService.generateEmbedding(request.text());
        return ResponseEntity.ok(embeddingVector);
    }
}

// --- DTOs (Data Transfer Objects) ---
record ChatRequest(String question) {
}

record ChatResponse(String answer) {
}

record EmbedRequest(String text) {
}
