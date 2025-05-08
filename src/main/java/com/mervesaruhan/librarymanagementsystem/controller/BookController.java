package com.mervesaruhan.librarymanagementsystem.controller;


import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.enums.BookSearchField;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import com.mervesaruhan.librarymanagementsystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Book Management", description = "Book CRUD Operations")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping
    @Operation(summary = "Save book-ROLE:LIBRARIAN")
    public ResponseEntity<RestResponse<BookDto>> saveBook(@RequestBody @Valid BookSaveRequestDto bookSaveRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.of(bookService.saveBook(bookSaveRequestDto)));
    }


    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping
    @Operation(summary = "Get all books with pagination-ROLE:LIBRARIAN/PATRON")
    public ResponseEntity<RestResponse<Page<BookDto>>> getAllBooks(@Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(RestResponse.of(bookService.findAllBooks(pageable)));
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping("/{id}")
    @Operation(summary = "Get book by using id-ROLE:LIBRARIAN/PATRON")
    public ResponseEntity<RestResponse<BookDto>> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(RestResponse.of(bookService.findById(id)));
    }


    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping("/search/filter")
    @Operation(summary = "search for a book containing the selected field and the keyword-ROLE:LIBRARIAN/PATRON")
    public ResponseEntity<RestResponse<Page<BookDto>>> searchBooks(@RequestParam String keyword, @Parameter(hidden = true) Pageable pageable, @RequestParam BookSearchField field){
        return ResponseEntity.ok(RestResponse.of(bookService.searchBooks(keyword, pageable, field)));
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping("/availability")
    @Operation(summary = "Get books by Inventory count to see availability-ROLE:LIBRARIAN/PATRON")
    public ResponseEntity<RestResponse<Page<BookDto>>> getBooksByAvailability(@Parameter(hidden = true) Pageable pageable, int count) {
        return ResponseEntity.ok(RestResponse.of(bookService.getBooksByAvailability(count, pageable)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}/inventoryCount/{count}")
    @Operation(summary = "updating book inventory count-ROLE:LIBRARIAN")
    public ResponseEntity<RestResponse<BookDto>> updateBookInventoryCount( @PathVariable @NotNull Long id, @PathVariable @NotNull Integer count) {
        return ResponseEntity.ok(RestResponse.of(bookService.updateInventory(id, count)));
    }


    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}")
    @Operation(summary = "update book by id-ROLE:LIBRARIAN")
    public ResponseEntity<RestResponse<BookDto>> updateBook(@RequestBody @Valid BookUpdateRequestDto bookUpdateRequestDto, @PathVariable @NotNull Long id) {
        return ResponseEntity.ok(RestResponse.of(bookService.updateBook(bookUpdateRequestDto,id)));
    }


    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by using id-ROLE:LIBRARIAN")
    public ResponseEntity<RestResponse<Void>> deleteBook(@PathVariable @NotNull Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(RestResponse.empty());
    }
}
