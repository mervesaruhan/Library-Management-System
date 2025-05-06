package com.mervesaruhan.librarymanagementsystem.kafka.producer;

import com.mervesaruhan.librarymanagementsystem.kafka.dto.KafkaLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaLoggerProducer {

    private final KafkaTemplate<String, KafkaLogMessage> kafkaTemplate;

    @Value("${library.kafka.topic:library-log-topic}")
    private String topic;

    public void sendLog(KafkaLogMessage message) {
        try {
            kafkaTemplate.send(topic, message);
            log.debug("Kafka log sent → {}", message);
        } catch (Exception e) {
            log.error("Failed to send log to Kafka", e);
        }
    }
}
