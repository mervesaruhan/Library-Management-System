package com.mervesaruhan.librarymanagementsystem.Tests.integration;


import com.mervesaruhan.librarymanagementsystem.general.BookTestDataGenerator;
import com.mervesaruhan.librarymanagementsystem.general.TestConstants;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;
import com.mervesaruhan.librarymanagementsystem.model.enums.BookSearchField;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidBookIdException;
import com.mervesaruhan.librarymanagementsystem.repository.BookRepository;
import com.mervesaruhan.librarymanagementsystem.Tests.BookService;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookServiceIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;
    @Autowired
    private LogHelper logHelper;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldSaveBookSuccessfully() {
        BookSaveRequestDto request = BookTestDataGenerator.createBookSaveRequestDto();

        BookDto savedBook = bookService.saveBook(request);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.isbn()).isEqualTo(TestConstants.TEST_BOOK_ISBN);
        assertThat(bookRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldThrowExceptionWhenSavingBookWithDuplicateIsbn() {
        BookSaveRequestDto request = BookTestDataGenerator.createBookSaveRequestDto();
        bookService.saveBook(request);

        assertThrows(IllegalArgumentException.class, () -> bookService.saveBook(request));
    }

    @Test
    void shouldReturnAllBooksWithPagination() {
        bookService.saveBook(BookTestDataGenerator.createBookSaveRequestDto());

        Page<BookDto> books = bookService.findAllBooks(PageRequest.of(0, 10));

        assertThat(books).isNotEmpty();
        assertThat(books.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldFindBookById() {
        Book saved = bookRepository.save(BookTestDataGenerator.createBook());

        BookDto result = bookService.findById(saved.getId());

        assertThat(result.title()).isEqualTo(TestConstants.TEST_BOOK_TITLE);
    }

    @Test
    void shouldThrowExceptionWhenBookIdNotFound() {
        assertThrows(InvalidBookIdException.class, () -> bookService.findById(99L));
    }

    @Test
    void shouldSearchBooksByTitle() {
        bookRepository.save(BookTestDataGenerator.createBook());

        Page<BookDto> result = bookService.searchBooks(
                TestConstants.TEST_BOOK_TITLE,
                PageRequest.of(0, 5),
                BookSearchField.TITLE
        );

        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).title()).containsIgnoringCase("book");
    }

    @Test
    void shouldThrowExceptionWhenSearchReturnsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                bookService.searchBooks("notfound", PageRequest
                        .of(0, 5), BookSearchField.AUTHOR)
        );
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        Book saved = bookRepository.save(BookTestDataGenerator.createBook());

        BookUpdateRequestDto updateDto = BookTestDataGenerator.createBookUpdateRequestDto();

        BookDto updated = bookService.updateBook(updateDto, saved.getId());

        assertThat(updated.description()).isEqualTo(TestConstants.TEST_BOOK_DESCRIPTION);
    }

    @Test
    void shouldUpdateInventorySuccessfully() {
        Book saved = bookRepository.save(BookTestDataGenerator.createBook());

        BookDto updated = bookService.updateInventory(saved.getId(), 99);

        assertThat(updated.inventoryCount()).isEqualTo(99);
    }

    @Test
    void shouldReturnBooksWithInventoryGreaterThanGivenCount() {
        bookRepository.save(BookTestDataGenerator.createBook());

        Page<BookDto> books = bookService.getBooksByAvailability(10, PageRequest.of(0, 5));

        assertThat(books).isNotEmpty();
    }

    @Test
    void shouldDeleteBookById() {
        Book saved = bookRepository.save(BookTestDataGenerator.createBook());

        bookService.deleteBook(saved.getId());

        assertThat(bookRepository.findById(saved.getId())).isEmpty();
    }


}