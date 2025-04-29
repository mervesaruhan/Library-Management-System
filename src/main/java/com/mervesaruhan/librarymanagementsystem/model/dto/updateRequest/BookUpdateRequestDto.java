package com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest;

import com.mervesaruhan.librarymanagementsystem.model.enums.AvailabilityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record BookUpdateRequestDto(
        @NotBlank(message =  "Başlık boş olamaz.")
        @Size(max = 250, min=2, message = "Kitap başlığı en az 2, en fazla 250 karakter olmalıdır.")
        String title,

        @NotBlank(message =  "Yazar bilgisi boş olamaz.")
        @Size(max = 250, min=2, message = "Yazar bilgisi en az 2, en fazla 250 karakter olmalıdır.")
        String author,

        @NotBlank(message =  "ISBN boş olamaz.")
        @Size(max = 11, min=11, message = "ISBN  11 karakter olmalıdır.")
        String isbn,

        @NotNull(message =  "Yayın tarihi boş olamaz.")
        @PastOrPresent(message = "Yayın tarihi geçmiş veya bugün olmalıdır")
        LocalDate publishedDate,

        @NotBlank(message =  "Yayınevi bilgisi boş olamaz.")
        @Size(max = 250, min=2, message = "Yayınevi bilgisi en az 2, en fazla 250 karakter olmalıdır.")
        String publisher,

        @NotBlank(message =  "Tür bilgisi boş olamaz.")
        @Size(max = 100, min=2, message = "Tür bilgisi en az 2, en fazla 100 karakter olmalıdır.")
        String genre,

        String description,

        @NotNull(message =  "Sayfa sayısı boş olamaz.")
        Integer pageCount,

        @NotNull(message =  "Müsaitlik durumu boş olamaz.")
        AvailabilityEnum availability

) {
}
