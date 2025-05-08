package com.mervesaruhan.librarymanagementsystem.model.exception;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "Genel hata detaylarını içeren veri yapısı")
public record GeneralMessagesForValidations(

        @Schema(description = "Hatanın oluştuğu zaman")
        LocalDateTime timestamp,

        @Schema(description = "Genel hata mesajı")
        String message,

        @Schema(description = "İstekle ilgili açıklama (örn: uri=/api/v1/...)")
        String description,

        @Schema(description = "Alan bazlı validation hataları")
        List<Map<String, String>> errors

) {}