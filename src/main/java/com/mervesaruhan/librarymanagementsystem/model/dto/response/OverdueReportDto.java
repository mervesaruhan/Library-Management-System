package com.mervesaruhan.librarymanagementsystem.model.dto.response;

import java.util.List;

public record OverdueReportDto(
        int totalBooks,
        int totalBorrowed,
        int totalReturned,
        int totalOverdue,
        List<BorrowingDto> overdueList
) {
}