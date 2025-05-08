package com.mervesaruhan.librarymanagementsystem.kafka.interceptor;

import com.mervesaruhan.librarymanagementsystem.kafka.dto.RequestLogMessage;
import com.mervesaruhan.librarymanagementsystem.kafka.producer.KafkaLoggerProducer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class LogInterceptor implements HandlerInterceptor {

    private final KafkaLoggerProducer kafkaLoggerProducer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        String username = "anonymous";
        try {
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception ignored) {}

        RequestLogMessage logMessage = RequestLogMessage.builder()
                .method(method)
                .uri(path)
                .username(username)
                .timestamp(LocalDateTime.now())

                // Döngüye girmemesi için kaynak bilgisi eklendi
                .source("app")
                .build();

        kafkaLoggerProducer.sendRequestLog(logMessage);

        return true;
    }
}