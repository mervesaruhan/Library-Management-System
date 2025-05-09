package com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record BookUpdateRequestDto(
        @NotBlank(message =  "Title cannot be blank.")
        @Size(max = 250, min = 2, message = "Book title must be between 2 and 250 characters.")
        String title,

        @NotBlank(message = "Author information cannot be blank.")
        @Size(max = 250, min=2, message = "message = \"Author information cannot be blank.")
        String author,

        @NotBlank(message =  "ISBN cannot be blank.")
        @Size(max = 11, min=11, message = "ISBN must be exactly 11 characters.")
        String isbn,

        @NotNull(message =  "Yayın tarihi boş olamaz.")
        @PastOrPresent(message = "Yayın tarihi geçmiş veya bugün olmalıdır")
        LocalDate publishedDate,

        @NotBlank(message =  "Publication date cannot be null.")
        @Size(max = 250, min=2, message = "Publisher information must be between 2 and 250 characters.")
        String publisher,

        @NotBlank(message =  "Genre cannot be blank.")
        @Size(max = 100, min=2, message = "Genre must be between 2 and 100 characters.")
        String genre,

        String description,

        @NotNull(message =  "Page count cannot be null.")
        @Positive(message =  "Page count must be greater than zero.")
        Integer pageCount,

        @NotNull(message =  "Inventory count cannot be null.")
        @Min(value = 0, message = "Inventory count must be equal zero or greater.")
        Integer inventoryCount
) {
}
