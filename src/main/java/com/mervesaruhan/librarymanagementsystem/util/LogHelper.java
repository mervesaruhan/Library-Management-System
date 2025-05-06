package com.mervesaruhan.librarymanagementsystem.util;

import com.mervesaruhan.librarymanagementsystem.kafka.dto.KafkaLogMessage;
import com.mervesaruhan.librarymanagementsystem.kafka.producer.KafkaLoggerProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
@RequiredArgsConstructor
public class LogHelper {

    private final KafkaLoggerProducer kafkaLoggerProducer;

    public void info(String format, Object... args) {
        log.info(format, args);
        sendToKafka("INFO", formatMessage(format, args));
    }

    public void warn(String format, Object... args) {
        log.warn(format, args);
        sendToKafka("WARN", formatMessage(format, args));
    }

    public void error(String format, Object... args) {
        log.error(format, args);
        sendToKafka("ERROR", formatMessage(format, args));
    }

    public void debug(String format, Object... args) {
        log.debug(format, args);
        sendToKafka("DEBUG", formatMessage(format, args));
    }

    private String formatMessage(String format, Object... args) {
        try {
            return String.format(format.replace("{}", "%s"), args);
        } catch (Exception e) {
            return format + " [LOG FORMAT ERROR]";
        }
    }

    private void sendToKafka(String level, String message) {
        String username = "anonymous";
        try {
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception ignored) {
        }

        KafkaLogMessage kafkaLog = KafkaLogMessage.builder()
                .level(level)
                .message(message)
                .source(detectCallingClass())
                .timestamp(LocalDateTime.now())
                .username(username)
                .build();

        kafkaLoggerProducer.sendLog(kafkaLog);
    }

    private String detectCallingClass() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {
            if (!element.getClassName().contains("LogHelper")) {
                return element.getClassName();
            }
        }
        return "UnknownSource";
    }
}