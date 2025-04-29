package com.mervesaruhan.librarymanagementsystem.controller;


import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import com.mervesaruhan.librarymanagementsystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Library Management System", description = "Book CRUD işlemleri")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Save book")
    public ResponseEntity<RestResponse<BookDto>> saveBook(@RequestBody @Valid BookSaveRequestDto bookSaveRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.of(bookService.saveBook(bookSaveRequestDto)));
    }


    @GetMapping
    @Operation(summary = "Get all books with pagination")
    public ResponseEntity<RestResponse<Page<BookDto>>> getAllBooks(@Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(RestResponse.of(bookService.findAllBooks(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by using id")
    public ResponseEntity<RestResponse<BookDto>> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(RestResponse.of(bookService.findById(id)));
    }

    @GetMapping("/search/filter")
    @Operation(summary = "search for a book containing the selected field and the keyword")
    public ResponseEntity<RestResponse<Page<BookDto>>> searchBooks(@RequestParam String keyword, @RequestParam Pageable pageable, @RequestParam String field){
        return ResponseEntity.ok(RestResponse.of(bookService.searchBooks(keyword, pageable, field)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "update book by id")
    public ResponseEntity<RestResponse<BookDto>> updateBook(@RequestBody @Valid BookUpdateRequestDto bookUpdateRequestDto, @PathVariable @NotNull Long id) {
        return ResponseEntity.ok(RestResponse.of(bookService.updateBook(bookUpdateRequestDto,id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by using id")
    public ResponseEntity<RestResponse<Void>> deleteBook(@PathVariable @NotNull Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(RestResponse.empty());
    }
}
