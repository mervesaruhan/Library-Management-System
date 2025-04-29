package com.mervesaruhan.librarymanagementsystem.model.dto.response;

import com.mervesaruhan.librarymanagementsystem.model.enums.AvailabilityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record BookDto(

        Long id,
        String title,
        String author,
        String isbn,
        LocalDate publishedDate,
        String publisher,
        String genre,
        String description,
        Integer pageCount,
        AvailabilityEnum availability

) {
}
