package com.mervesaruhan.librarymanagementsystem.controller;


import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowingDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BorrowingSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.enums.BorrowingStatusEnum;
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

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Borrowing Management", description = "Borrowing CRUD operations")
@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/v1/borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;



    @PreAuthorize("hasRole('PATRON')")
    @PostMapping
    @Operation(summary = "Borrowing operation- ROLE: PATRON")
    public  ResponseEntity<RestResponse<BorrowingDto>> saveBorrowing(@Valid @RequestBody BorrowingSaveRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.of(borrowingService.saveBorrowing(requestDto)));
    }

    @PutMapping("/{borrowing_id}")
    @PreAuthorize("hasRole('PATRON')")
    @Operation(summary = "Return operation- ROLE: PATRON")
    public ResponseEntity<RestResponse<BorrowingDto>> returnBorrowing(@PathVariable long borrowing_id) {
        return ResponseEntity.ok(RestResponse.of(borrowingService.returnBook(borrowing_id)));
    }

    @PutMapping("/updateDueDate/{borrowing_id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @Operation(summary = "Update Due Date- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<BorrowingDto>> updateDueDate(@PathVariable long borrowing_id, LocalDate dueDate) {
        return ResponseEntity.ok(RestResponse.of(borrowingService.updateDueDate(borrowing_id, dueDate)));
    }


    @GetMapping("/history/my")
    @PreAuthorize("hasRole('PATRON')")
    @Operation(summary = "Get borrowing history for the logged-in user- ROLE: PATRON")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getMyBorrowingHistory() {
        return ResponseEntity.ok(RestResponse.of(borrowingService.getMyBorrowingHistory()));
    }


    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/history/all")
    @Operation(summary = "Get all borrowing history- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getAllBorrowingHistory() {
        return ResponseEntity.ok(RestResponse.of(borrowingService.getAllBorrowingHistory()));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/{status}")
    @Operation(summary = "Get all borrowings by status- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getBorrowingsByStatus(@PathVariable BorrowingStatusEnum status) {
        return ResponseEntity.ok(RestResponse.of(borrowingService.getBorrowingsByStatus(status)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/overdue")
    @Operation(summary = "Get all overdue borrowings- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<List<BorrowingDto>>> getOverdueBorrowings() {
        return ResponseEntity.ok(RestResponse.of(borrowingService.getAndUpdateOverdueBorrowings()));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/overdue/report")
    @Operation(summary = "Get detailed overdue report for librarians- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<String>> overdueBorrowingsReport() {
        return ResponseEntity.ok(RestResponse.of(borrowingService.generateOverdueReport()));
    }
}
