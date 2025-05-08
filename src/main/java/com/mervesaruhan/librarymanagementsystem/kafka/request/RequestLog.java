package com.mervesaruhan.librarymanagementsystem.kafka.request;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "request_log")
public class RequestLog {

    @Id
    @GeneratedValue(generator = "requestLog", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "requestLog", sequenceName = "request_log_id_seq")
    private Long id;

    private LocalDateTime date;

    private String message;

    private String description;
}