package com.journal.mental_health_journal.service;


import com.journal.mental_health_journal.dto.JournalEntryRequest;
import com.journal.mental_health_journal.dto.JournalEntryResponse;

import java.util.List;

public interface JournalService {
    JournalEntryResponse createEntry(JournalEntryRequest request);
    JournalEntryResponse getEntryById(Long id);
    List<JournalEntryResponse> getEntriesByUserId(Long userId);
}
