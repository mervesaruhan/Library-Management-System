package com.mervesaruhan.librarymanagementsystem.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaLogMessage {
    private String level; // INFO, WARN, ERROR
    private String message;
    private String username; // o anda aktif kullanıcı
    private LocalDateTime timestamp;
    private String source; // örnek: "BookService", "BorrowingService"
}