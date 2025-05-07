package com.mervesaruhan.librarymanagementsystem.util;

import com.mervesaruhan.librarymanagementsystem.kafka.dto.ErrorLogMessage;
import com.mervesaruhan.librarymanagementsystem.kafka.dto.RequestLogMessage;
import com.mervesaruhan.librarymanagementsystem.kafka.producer.KafkaLoggerProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
public class LogHelper {

    private final KafkaLoggerProducer kafkaLoggerProducer;
    public LogHelper(@Autowired(required = false) KafkaLoggerProducer kafkaLoggerProducer) {
        this.kafkaLoggerProducer = kafkaLoggerProducer;
    }

    public void info(String format, Object... args) {
        log.info(format, args);
        try {
            sendRequestToKafka("INFO", format, args);
        } catch (Exception e) {
            log.warn("[Kafka UNAVAILABLE] " + format, args);
        }
    }

    public void warn(String format, Object... args) {
        log.warn(format, args);
        try {
            sendRequestToKafka("WARN", format, args);
        } catch (Exception e) {
            log.warn("[Kafka UNAVAILABLE] " + format, args);
        }
    }

    public void error(String format, Object... args) {
        log.error(format, args);
        try {
            sendErrorToKafka(format, args);
        } catch (Exception e) {
            log.error("[Kafka UNAVAILABLE] " + format, args);
        }
    }

    public void debug(String format, Object... args) {
        log.debug(format, args);
        try {
            sendRequestToKafka("DEBUG", format, args);
        } catch (Exception e) {
            log.debug("[Kafka UNAVAILABLE] " + format, args);
        }
    }


    private void sendRequestToKafka(String level, String format, Object... args) {
        RequestLogMessage logMessage = RequestLogMessage.builder()
                .method(level)
                .uri(String.format(format, args))
                .username(getCurrentUsername())
                .timestamp(LocalDateTime.now())
                .build();
        kafkaLoggerProducer.sendRequestLog(logMessage);
    }

    private void sendErrorToKafka(String format, Object... args) {
        ErrorLogMessage errorLog = ErrorLogMessage.builder()
                .message(String.format(format, args))
                .username(getCurrentUsername())
                .timestamp(LocalDateTime.now())
                .build();
        kafkaLoggerProducer.sendErrorLog(errorLog);
    }

    private String getCurrentUsername() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return "anonymous";
        }
    }
}
