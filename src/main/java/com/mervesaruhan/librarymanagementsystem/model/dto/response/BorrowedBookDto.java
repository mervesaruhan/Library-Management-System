package com.mervesaruhan.librarymanagementsystem.model.dto.response;

public record BorrowedBookDto(
        Long id,
        String title,
        String author
) {
}
