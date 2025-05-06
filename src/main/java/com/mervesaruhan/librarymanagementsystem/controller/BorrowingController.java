package com.mervesaruhan.librarymanagementsystem.controller;


import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowingDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BorrowingSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import com.mervesaruhan.librarymanagementsystem.service.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Borrowing Management", description = "Borrowing CRUD işlemleri")
@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/v1/borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;
    private final LogHelper logHelper;



    @PreAuthorize("hasRole('PATRON')")
    @PostMapping
    @Operation(summary = "Borrowing operation")
    public  ResponseEntity<RestResponse<BorrowingDto>> saveBorrowing(@Valid @RequestBody BorrowingSaveRequestDto requestDto) {
        logHelper.info("Borrowing requested by Patron");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.of(borrowingService.saveBorrowing(requestDto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATRON')")
    @Operation(summary = "Return operation")
    public ResponseEntity<RestResponse<BorrowingDto>> returnBorrowing(@PathVariable long id) {
        logHelper.info("Return requested for borrowing ID: {}", id);
        return ResponseEntity.ok(RestResponse.of(borrowingService.returnBook(id)));
    }


    @GetMapping("/history/my")
    @PreAuthorize("hasRole('PATRON')")
    @Operation(summary = "Get borrowing history for the logged-in user")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getMyBorrowingHistory() {
        logHelper.debug("Fetching borrowing history for current user");
        return ResponseEntity.ok(RestResponse.of(borrowingService.getMyBorrowingHistory()));
    }


    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/history/all")
    @Operation(summary = "Get all borrowing history (only for librarians)")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getAllBorrowingHistory() {
        logHelper.debug("Fetching all borrowing history requested by Librarian");
        return ResponseEntity.ok(RestResponse.of(borrowingService.getAllBorrowingHistory()));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/overdue")
    @Operation(summary = "Get all overdue borrowings")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getOverdueBorrowings() {
        logHelper.debug("Fetching overdue borrowings");
        return ResponseEntity.ok(RestResponse.of(borrowingService.getOverdueBorrowings()));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/overdue/report")
    @Operation(summary = "Get detailed overdue report for librarians")
    public ResponseEntity<RestResponse<String>> overdueBorrowingsReport() {
        logHelper.info("Overdue report generation requested by Librarian");
        return ResponseEntity.ok(RestResponse.of(borrowingService.generateOverdueReport()));
    }
}
