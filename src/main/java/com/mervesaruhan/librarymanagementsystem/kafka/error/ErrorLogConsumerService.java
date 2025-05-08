package com.mervesaruhan.librarymanagementsystem.kafka.error;

import com.mervesaruhan.librarymanagementsystem.kafka.dto.ErrorLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class ErrorLogConsumerService {

    private final ErrorLogRepository errorLogRepository;

    @KafkaListener(
            topics = "errorLog",
            groupId = "log-consumer-group",
            containerFactory = "errorLogKafkaListenerContainerFactory")
    public void consumeErrorLogs(ErrorLogMessage message) {

        message.setSource("consumer");

        ErrorLog errorLog = new ErrorLog();
        errorLog.setDate(message.getTimestamp());
        errorLog.setMessage(message.getMessage());
        errorLog.setDescription("Error from user: " + message.getUsername());

        errorLogRepository.save(errorLog);
    }
}