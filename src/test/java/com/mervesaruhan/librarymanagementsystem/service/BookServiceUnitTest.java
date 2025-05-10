package com.mervesaruhan.librarymanagementsystem.service;


import com.mervesaruhan.librarymanagementsystem.general.BookTestDataGenerator;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;
import com.mervesaruhan.librarymanagementsystem.model.enums.BookSearchField;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidBookIdException;
import com.mervesaruhan.librarymanagementsystem.model.mapper.BookMapper;
import com.mervesaruhan.librarymanagementsystem.repository.BookRepository;
import org.mockito.quality.Strictness;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    void shouldSearchBooksByTitleSuccessfully() {
        String keyword = "java";
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchField field = BookSearchField.TITLE;

        Book book = BookTestDataGenerator.createBook();
        BookDto bookDto = BookTestDataGenerator.createBookDto();

        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findByTitleContainingIgnoreCase(keyword, pageable)).thenReturn(bookPage);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);
        doNothing().when(logHelper).info(anyString(), (Object[]) any());

        Page<BookDto> result = bookService.searchBooks(keyword, pageable, field);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(bookDto);

        verify(bookRepository).findByTitleContainingIgnoreCase(keyword, pageable);
        verify(bookMapper).toBookDto(book);
        verify(logHelper).info(
                eq("Searching books with keyword: {}, field: {}"),
                eq("java"),
                eq(BookSearchField.TITLE)
        );
    }

    @Test
    void shouldThrowExceptionWhenNoBooksFound() {
        String keyword = "unknown";
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchField field = BookSearchField.AUTHOR;

        Page<Book> emptyPage = Page.empty(pageable);

        when(bookRepository.findByAuthorContainingIgnoreCase(keyword, pageable)).thenReturn(emptyPage);
        doNothing().when(logHelper).info(anyString(), (Object[]) any());
        doNothing().when(logHelper).warn(anyString(), (Object[]) any());

        assertThatThrownBy(() -> bookService.searchBooks(keyword, pageable, field))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No books found for the given keyword");

        verify(logHelper).warn(anyString(), (Object[]) any());
    }


    @Test
    void shouldDeleteBookSuccessfully() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        bookService.deleteBook(book.getId());

        verify(bookRepository).delete(book);
    }
}
