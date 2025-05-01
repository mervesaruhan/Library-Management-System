package com.mervesaruhan.librarymanagementsystem.model.dto.response;

import com.mervesaruhan.librarymanagementsystem.model.enums.BorrowingStatusEnum;

import java.time.LocalDate;

public record BorrowingDto(
        Long id,
        String userFullName,
        Long userId,
        String bookTitle,
        Long bookId,
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate,
        BorrowingStatusEnum status
) {
}
