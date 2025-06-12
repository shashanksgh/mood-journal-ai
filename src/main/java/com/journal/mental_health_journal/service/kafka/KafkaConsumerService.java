package com.journal.mental_health_journal.service.kafka;

import com.journal.mental_health_journal.entity.JournalAnalysis;
import com.journal.mental_health_journal.entity.JournalEntry;
import com.journal.mental_health_journal.repository.JournalAnalysisRepository;
import com.journal.mental_health_journal.repository.JournalEntryRepository;
import com.journal.mental_health_journal.service.llm.OpenAiService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class KafkaConsumerService {

    private final OpenAiService openAiService;
    private final JournalEntryRepository journalEntryRepository;
    private final JournalAnalysisRepository journalAnalysisRepository;

    public KafkaConsumerService(OpenAiService openAiService,
                                JournalEntryRepository journalEntryRepository,
                                JournalAnalysisRepository journalAnalysisRepository) {
        this.openAiService = openAiService;
        this.journalEntryRepository = journalEntryRepository;
        this.journalAnalysisRepository = journalAnalysisRepository;
    }

    @KafkaListener(topics = "${kafka.topic.journal}", groupId = "mood-analyzer")
    @Transactional
    public void consume(String message) {
        try {
            // Message format: "journalEntryId::entryContent"
            String[] parts = message.split("::", 2);
            if (parts.length != 2) {
                System.err.println("Invalid message format received: " + message);
                return;
            }

            Long journalEntryId = Long.parseLong(parts[0]);
            String content = parts[1];

            // Analyze mood using OpenAI
            String sentiment = openAiService.analyzeMood(journalEntryId, content);

            // Fetch the JournalEntry from DB
            Optional<JournalEntry> optionalEntry = journalEntryRepository.findById(journalEntryId);
            if (optionalEntry.isPresent()) {
                JournalEntry entry = optionalEntry.get();

                // Create and save JournalAnalysis
                JournalAnalysis analysis = new JournalAnalysis();
                analysis.setJournalEntry(entry);
                analysis.setSentiment(sentiment);
//                analysis.setAnalyzedAt(LocalDateTime.now());

                journalAnalysisRepository.save(analysis);

                System.out.println("Mood analysis saved for entry ID: " + journalEntryId);
            } else {
                System.err.println("JournalEntry not found for ID: " + journalEntryId);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging
        }
    }
}
