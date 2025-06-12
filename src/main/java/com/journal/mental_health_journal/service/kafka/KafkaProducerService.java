package com.journal.mental_health_journal.service.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.journal}")
    private String journalTopic;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Long journalEntryId, String content) {
        String message = journalEntryId + "::" + content;
        kafkaTemplate.send(journalTopic, message);
        System.out.println("Published to Kafka: " + message);
    }
}
