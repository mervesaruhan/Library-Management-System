package com.mervesaruhan.librarymanagementsystem.model.dto.response;

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
        Integer inventoryCount,
        Boolean available
) {
}
