package com.journal.mental_health_journal.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.journal.mental_health_journal.entity.JournalAnalysis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
public class OpenAiAnalyzer {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JournalAnalysis analyze(String content) {
        String prompt = """
            Analyze the emotional tone of this journal entry. Return JSON with fields:
            sentiment (Positive/Neutral/Negative),
            score (0.0 - 1.0),
            summary,
            and 3 themes (comma separated string).

            Entry: %s
        """.formatted(content);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        String requestBody = """
        {
          "model": "gpt-3.5-turbo",
          "messages": [
            { "role": "user", "content": "%s" }
          ],
          "temperature": 0.7
        }
        """.formatted(prompt);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                entity,
                String.class
        );

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String reply = root
                    .path("choices").get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // TODO: Parse OpenAI JSON reply in content (if you returned JSON string from prompt)
            JsonNode result = objectMapper.readTree(reply);

            JournalAnalysis analysis = new JournalAnalysis();
            analysis.setSentiment(result.path("sentiment").asText());
            analysis.setScore(result.path("score").asDouble());
            analysis.setSummary(result.path("summary").asText());
            analysis.setThemes(Collections.singletonList(result.path("themes").asText()));

            return analysis;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenAI response", e);
        }
    }
}
