package com.journal.mental_health_journal.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "journal_analyses")
public class JournalAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<String> getThemes() {
        return themes;
    }

    public void setThemes(List<String> themes) {
        this.themes = themes;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public JournalEntry getJournalEntry() {
        return journalEntry;
    }

    public void setJournalEntry(JournalEntry journalEntry) {
        this.journalEntry = journalEntry;
    }

    private String sentiment; // e.g., "positive", "neutral", "negative"
    private double score;     // e.g., sentiment score -1 to 1

    @ElementCollection
    private List<String> themes; // e.g., ["anxiety", "work stress"]

    private String summary; // LLM-generated short summary

    @OneToOne
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry;
}
