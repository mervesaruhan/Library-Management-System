package com.mervesaruhan.librarymanagementsystem.controller;


import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowingDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.OverdueReportDto;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import com.mervesaruhan.librarymanagementsystem.service.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Borrowing Management", description = "Borrowing CRUD işlemleri")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;



    @GetMapping("/history/user/{userId}")
    @Operation(summary = "Get borrowing history by user")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getBorrowingHistoryByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(RestResponse.of(borrowingService.getBorrowingHistoryByUser(userId)));
    }

    @GetMapping("/history/all")
    @Operation(summary = "Get all borrowing history (only for librarians)")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getAllBorrowingHistory() {
        return ResponseEntity.ok(RestResponse.of(borrowingService.getAllBorrowingHistory()));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get all overdue borrowings")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getOverdueBorrowings() {
        return ResponseEntity.ok(RestResponse.of(borrowingService.getOverdueBorrowings()));
    }

    @GetMapping("/overdue/report")
    @Operation(summary = "Get detailed overdue report for librarians")
    public ResponseEntity<RestResponse<OverdueReportDto>> overdueBorrowingsReport() {
        return ResponseEntity.ok(RestResponse.of(borrowingService.getOverdueReport()));
    }
}
