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



    @PreAuthorize("hasRole('PATRON')")
    @PostMapping
    @Operation(summary = "Borrowing operation",description = "ROLE:PATRON")
    public  ResponseEntity<RestResponse<BorrowingDto>> saveBorrowing(@Valid @RequestBody BorrowingSaveRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.of(borrowingService.saveBorrowing(requestDto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATRON')")
    @Operation(summary = "Return operation",description = "ROLE:PATRON")
    public ResponseEntity<RestResponse<BorrowingDto>> returnBorrowing(@PathVariable long id) {
        return ResponseEntity.ok(RestResponse.of(borrowingService.returnBook(id)));
    }


    @GetMapping("/history/my")
    @PreAuthorize("hasRole('PATRON')")
    @Operation(summary = "Get borrowing history for the logged-in user",description = "ROLE:PATRON")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getMyBorrowingHistory() {
        return ResponseEntity.ok(RestResponse.of(borrowingService.getMyBorrowingHistory()));
    }


    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/history/all")
    @Operation(summary = "Get all borrowing history (only for librarians)",description = "ROLE:LIBRARIAN")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getAllBorrowingHistory() {
        return ResponseEntity.ok(RestResponse.of(borrowingService.getAllBorrowingHistory()));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/overdue")
    @Operation(summary = "Get all overdue borrowings",description = "ROLE:LIBRARIAN")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getOverdueBorrowings() {
        return ResponseEntity.ok(RestResponse.of(borrowingService.getOverdueBorrowings()));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/overdue/report")
    @Operation(summary = "Get detailed overdue report for librarians",description = "ROLE:LIBRARIAN")
    public ResponseEntity<RestResponse<String>> overdueBorrowingsReport() {
        return ResponseEntity.ok(RestResponse.of(borrowingService.generateOverdueReport()));
    }
}
