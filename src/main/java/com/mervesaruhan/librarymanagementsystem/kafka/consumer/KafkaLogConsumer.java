package com.mervesaruhan.librarymanagementsystem.kafka.consumer;

import com.mervesaruhan.librarymanagementsystem.kafka.dto.KafkaLogMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaLogConsumer {

    @KafkaListener(
            topics = "${library.kafka.topic:library-log-topic}",
            groupId = "${library.kafka.group-id:library-log-group}"
    )
    public void consume(KafkaLogMessage message) {
        log.info(" Kafka Log Received → [{}] {} | source: {} | user: {} | at: {}",
                message.getLevel(),
                message.getMessage(),
                message.getSource(),
                message.getUsername(),
                message.getTimestamp()
        );
    }
}
