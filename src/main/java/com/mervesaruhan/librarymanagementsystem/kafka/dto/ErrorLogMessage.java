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
public class ErrorLogMessage {
    private String message;
    private String username;
    private LocalDateTime timestamp;
}