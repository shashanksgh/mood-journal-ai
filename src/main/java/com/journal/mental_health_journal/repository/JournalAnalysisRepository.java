package com.journal.mental_health_journal.repository;

import com.journal.mental_health_journal.entity.JournalAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalAnalysisRepository extends JpaRepository<JournalAnalysis, Long> {
}
