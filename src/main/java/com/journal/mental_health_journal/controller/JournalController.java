package com.journal.mental_health_journal.controller;

import com.journal.mental_health_journal.dto.JournalEntryRequest;
import com.journal.mental_health_journal.dto.JournalEntryResponse;
import com.journal.mental_health_journal.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @PostMapping
    public ResponseEntity<JournalEntryResponse> createEntry(@RequestBody JournalEntryRequest request) {
        JournalEntryResponse response = journalService.createEntry(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntryResponse> getEntryById(@PathVariable Long id) {
        return ResponseEntity.ok(journalService.getEntryById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JournalEntryResponse>> getEntriesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(journalService.getEntriesByUserId(userId));
    }

}
