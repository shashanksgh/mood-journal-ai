package com.journal.mental_health_journal.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "journal_entries")
public class JournalEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // You can make a User entity later

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public JournalAnalysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(JournalAnalysis analysis) {
        this.analysis = analysis;
    }

    @OneToOne(mappedBy = "journalEntry", cascade = CascadeType.ALL)
    private JournalAnalysis analysis;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
