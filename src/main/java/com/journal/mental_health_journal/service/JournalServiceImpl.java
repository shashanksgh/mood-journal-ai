package com.journal.mental_health_journal.service;

import com.journal.mental_health_journal.dto.JournalEntryRequest;
import com.journal.mental_health_journal.dto.JournalEntryResponse;
import com.journal.mental_health_journal.entity.JournalEntry;
import com.journal.mental_health_journal.repository.JournalEntryRepository;
import com.journal.mental_health_journal.service.kafka.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JournalServiceImpl implements JournalService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Override
    public JournalEntryResponse createEntry(JournalEntryRequest request) {
        JournalEntry entry = new JournalEntry();
        entry.setUserId(request.getUserId());
        entry.setContent(request.getContent());
        LocalTime currentTime = LocalTime.now();
        entry.setCreatedAt(LocalDateTime.parse(currentTime.toString()));

        JournalEntry saved = journalEntryRepository.save(entry);

        // TODO: can be made async
        kafkaProducerService.sendMessage(saved.getId(), saved.getContent());

        JournalEntryResponse response = new JournalEntryResponse();
        response.setId(saved.getId());
        response.setUserId(saved.getUserId());
        response.setContent(saved.getContent());
        response.setCreatedAt(LocalDateTime.parse(currentTime.toString()));

        return response;
    }

    @Override
    public JournalEntryResponse getEntryById(Long id) {
        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        return mapToResponse(entry);
    }

    @Override
    public List<JournalEntryResponse> getEntriesByUserId(Long userId) {
        List<JournalEntry> entries = journalEntryRepository.findAll()
                .stream()
                .filter(e -> e.getUserId().equals(userId))
                .collect(Collectors.toList());

        return entries.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔁 Utility method
    private JournalEntryResponse mapToResponse(JournalEntry entry) {
        JournalEntryResponse res = new JournalEntryResponse();
        res.setId(entry.getId());
        res.setUserId(entry.getUserId());
        res.setContent(entry.getContent());
        res.setCreatedAt(entry.getCreatedAt());

        if (entry.getAnalysis() != null) {
            res.setSentiment(entry.getAnalysis().getSentiment());
            res.setScore(entry.getAnalysis().getScore());
            res.setThemes(entry.getAnalysis().getThemes());
            res.setSummary(entry.getAnalysis().getSummary());
        }

        return res;
    }

}
