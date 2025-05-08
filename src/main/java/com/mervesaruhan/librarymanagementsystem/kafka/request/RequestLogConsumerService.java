package com.mervesaruhan.librarymanagementsystem.kafka.request;

import com.mervesaruhan.librarymanagementsystem.kafka.dto.RequestLogMessage;
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
public class RequestLogConsumerService {

    private final RequestLogRepository requestLogRepository;

    @KafkaListener(topics = "requestLog",
            groupId = "log-consumer-group",
            containerFactory = "requestLogKafkaListenerContainerFactory")
    public void consumeInfos(RequestLogMessage message){

        message.setSource("consumer");

        RequestLog requestLog = new RequestLog();
        requestLog.setDate(message.getTimestamp());
        requestLog.setMessage(message.getMethod() + " " + message.getUri());
        requestLog.setDescription("Request from user: " + message.getUsername());

        requestLogRepository.save(requestLog);
    }
}