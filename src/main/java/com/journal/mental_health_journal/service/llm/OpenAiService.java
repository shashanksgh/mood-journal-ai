package com.journal.mental_health_journal.service.llm;

import com.journal.mental_health_journal.entity.JournalAnalysis;
import com.journal.mental_health_journal.repository.JournalAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class OpenAiService {

    @Autowired
    JournalAnalysisRepository journalAnalysisRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String analyzeMood(Long journalEntryId, String content) {
        String prompt = "Classify the mood of the following journal entry in one word (e.g., happy, sad, anxious, calm):\n" + content;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> request = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", new Object[] {
                        Map.of("role", "user", "content", prompt)
                },
                "max_tokens", 10
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions", entity, Map.class);

            // TODO: refactor
            String mood = (String) ((Map)((Map)((java.util.List) response.getBody().get("choices")).get(0)).get("message")).get("content");

            JournalAnalysis journalAnalysis = new JournalAnalysis();
            journalAnalysis.setSentiment(mood.trim().toLowerCase());
            journalAnalysis.setId(journalEntryId);
            journalAnalysisRepository.save(journalAnalysis);

            return mood.trim().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }
}
