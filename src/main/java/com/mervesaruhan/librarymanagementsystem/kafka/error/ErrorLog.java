package com.mervesaruhan.librarymanagementsystem.kafka.error;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "error_log")
public class ErrorLog {

    @Id
    @GeneratedValue(generator = "ErrorLog", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ErrorLog", sequenceName = "ERROR_LOG_ID_SEQ")
    private Long id;

    private LocalDateTime date;

    private String message;

    private String description;

}