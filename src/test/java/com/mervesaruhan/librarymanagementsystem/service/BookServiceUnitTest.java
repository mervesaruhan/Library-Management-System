package com.mervesaruhan.librarymanagementsystem.service;


import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidBookIdException;
import com.mervesaruhan.librarymanagementsystem.model.mapper.BookMapper;
import com.mervesaruhan.librarymanagementsystem.repository.BookRepository;
import com.mervesaruhan.librarymanagementsystem.general.BookTestDataGenerator;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceUnitTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private LogHelper logHelper;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        book = BookTestDataGenerator.createBook();
        bookDto = BookTestDataGenerator.createBookDto();
    }

    @Test
    void shouldSaveBookSuccessfully() {
        BookSaveRequestDto request = BookTestDataGenerator.createBookSaveRequestDto();

        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(false);
        when(bookMapper.toBookEntity(request)).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);
        when(bookRepository.save(book)).thenReturn(book);

        BookDto result = bookService.saveBook(request);

        assertThat(result).isEqualTo(bookDto);
        verify(bookRepository).save(book);
    }

    @Test
    void shouldThrowExceptionWhenIsbnAlreadyExists() {
        BookSaveRequestDto request = BookTestDataGenerator.createBookSaveRequestDto();

        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(true);

        assertThatThrownBy(() -> bookService.saveBook(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ISBN");
    }

    @Test
    void shouldReturnAllBooksPaginated() {
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        Pageable pageable = PageRequest.of(0, 10);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.findAllBooks(pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldFindBookById() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        BookDto result = bookService.findById(book.getId());

        assertThat(result).isEqualTo(bookDto);
    }

    @Test
    void shouldThrowWhenBookIdNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(999L))
                .isInstanceOf(InvalidBookIdException.class);
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        BookUpdateRequestDto updateDto = BookTestDataGenerator.createBookUpdateRequestDto();

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookRepository.existsByIsbnAndIdNot(updateDto.isbn(), book.getId())).thenReturn(false);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        BookDto result = bookService.updateBook(updateDto, book.getId());

        assertThat(result).isEqualTo(bookDto);
    }

    @Test
    void shouldUpdateInventorySuccessfully() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        BookDto result = bookService.updateInventory(book.getId(), 88);

        assertThat(result).isEqualTo(bookDto);
        assertThat(book.getInventoryCount()).isEqualTo(88);
    }

    @Test
    void shouldReturnBooksWithAvailableInventory() {
        Pageable pageable = PageRequest.of(0, 5);

        Book book = BookTestDataGenerator.createBook();
        BookDto bookDto = BookTestDataGenerator.createBookDto();

        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findBooksByInventoryCountGreaterThan(0, pageable)).thenReturn(bookPage);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.getBooksByAvailability(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(bookDto);

        verify(bookRepository).findBooksByInventoryCountGreaterThan(0, pageable);
        verify(bookMapper).toBookDto(book);
    }


    @Test
    void shouldDeleteBookSuccessfully() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        bookService.deleteBook(book.getId());

        verify(bookRepository).delete(book);
    }
}
