package com.mervesaruhan.librarymanagementsystem.kafka.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestLogMessage {
    private String method;
    private String uri;
    private String username;
    private LocalDateTime timestamp;
}