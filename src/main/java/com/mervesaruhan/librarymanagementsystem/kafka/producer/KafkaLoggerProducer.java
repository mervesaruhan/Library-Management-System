package com.mervesaruhan.librarymanagementsystem.kafka.producer;

import com.mervesaruhan.librarymanagementsystem.kafka.dto.ErrorLogMessage;
import com.mervesaruhan.librarymanagementsystem.kafka.dto.RequestLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class KafkaLoggerProducer {

    private final KafkaTemplate<String, RequestLogMessage> requestKafkaTemplate;
    private final KafkaTemplate<String, ErrorLogMessage> errorKafkaTemplate;

    public void sendRequestLog(RequestLogMessage message) {
        // Loop'u engelle: Eğer mesaj consumer'dan geliyorsa Kafka'ya tekrar gönderme
        if ("consumer".equalsIgnoreCase(message.getSource())) {
            log.debug("Kafka loop prevented for request log: {}", message);
            return;
        }

        try {
            requestKafkaTemplate.send("requestLog", message);
            log.debug("Request log sent to Kafka → {}", message);
        } catch (Exception e) {
            log.error("Failed to send request log to Kafka", e);
        }
    }

    public void sendErrorLog(ErrorLogMessage message) {
        // loop engelle
        if ("consumer".equalsIgnoreCase(message.getSource())) {
            log.debug("Kafka loop prevented for error log: {}", message);
            return;
        }

        try {
            errorKafkaTemplate.send("errorLog", message);
            log.debug("Error log sent to Kafka → {}", message);
        } catch (Exception e) {
            log.error("Failed to send error log to Kafka", e);
        }
    }
}
