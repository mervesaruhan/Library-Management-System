package com.mervesaruhan.librarymanagementsystem.service;

import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;
import com.mervesaruhan.librarymanagementsystem.model.enums.BookSearchField;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidBookIdException;
import com.mervesaruhan.librarymanagementsystem.model.mapper.BookMapper;
import com.mervesaruhan.librarymanagementsystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final LogHelper logHelper;

    public BookDto saveBook(BookSaveRequestDto saveRequestDto){

        logHelper.info("Attempting to save book with title: {}, author: {}, isbn: {}",
                saveRequestDto.title(), saveRequestDto.author(), saveRequestDto.isbn());

        // ISBN benzersizlik kontrolü
        if (bookRepository.existsByIsbn(saveRequestDto.isbn())) {
            logHelper.error("Duplicate ISBN found while saving book: {}", saveRequestDto.isbn());
            throw new IllegalArgumentException("This ISBN number is already associated with a registered book.");
        }

        Book book = bookMapper.toBookEntity(saveRequestDto);

        bookRepository.save(book);
        logHelper.info("Book saved successfully. Generated ID: {}", book.getId());
        return bookMapper.toBookDto(book);
    }



    public Page<BookDto> findAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toBookDto);
    }


    public BookDto findById(Long id){
        return bookRepository.findById(id).map(bookMapper::toBookDto)
                .orElseThrow(() -> new InvalidBookIdException(id));
    }


    public Page<BookDto> searchBooks(String keyword, Pageable pageable, BookSearchField field) {
        logHelper.info("Searching books with keyword: {}, field: {}", keyword, field);
        Page<Book> books = switch (field) {
            case TITLE -> bookRepository.findByTitleContainingIgnoreCase(keyword, pageable);
            case AUTHOR -> bookRepository.findByAuthorContainingIgnoreCase(keyword, pageable);
            case ISBN -> bookRepository.findByIsbnContainingIgnoreCase(keyword, pageable);
            case GENRE -> bookRepository.findByGenreContainingIgnoreCase(keyword, pageable);
        };

        if (books.isEmpty()) {
            logHelper.warn("No books found for keyword: {}", keyword);
            throw new IllegalArgumentException("No books found for the given keyword: " + keyword);
        }

        return books.map(bookMapper::toBookDto);
    }



    public BookDto updateBook(BookUpdateRequestDto updateRequestDto, Long id){
        logHelper.info("Updating book with ID: {}", id);
        Book book= bookRepository.findById(id)
                .orElseThrow(() -> new InvalidBookIdException(id));

        //Başka bir kitapta bu isbn var mı kontrolü
        if (bookRepository.existsByIsbnAndIdNot(updateRequestDto.isbn(), id)) {
            logHelper.error("Duplicate ISBN found while updating book: {}", updateRequestDto.isbn());
            throw new IllegalArgumentException("This ISBN number is already associated with a registered book.");
        }

        book.setIsbn(updateRequestDto.isbn());
        book.setAuthor(updateRequestDto.author());
        book.setTitle(updateRequestDto.title());
        book.setPublisher(updateRequestDto.publisher());
        book.setPublishedDate(updateRequestDto.publishedDate());
        book.setGenre(updateRequestDto.genre());
        book.setInventoryCount(updateRequestDto.inventoryCount());
        book.setDescription(updateRequestDto.description());
        book.setPageCount(updateRequestDto.pageCount());

        bookRepository.save(book);
        logHelper.info("Book updated successfully. ID: {}", book.getId());
        return bookMapper.toBookDto(book);

    }


    public BookDto updateInventory(Long id, int inventoryCount){
        logHelper.info("Updating inventory for book ID: {} to count: {}", id, inventoryCount);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new InvalidBookIdException(id));
        book.setInventoryCount(inventoryCount);
        bookRepository.save(book);
        logHelper.info("Inventory updated for book ID: {} to count: {}", id, inventoryCount);
        return bookMapper.toBookDto(book);
    }


    public Page<BookDto> getBooksByAvailability(int count, Pageable pageable){
        Page<Book> books = bookRepository.findBooksByInventoryCountGreaterThan(count, pageable);
        return books.map(bookMapper::toBookDto);
    }


    public void deleteBook(Long id){
        logHelper.info("Deleting book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new InvalidBookIdException(id));
        bookRepository.delete(book);
        logHelper.info("Book deleted successfully. ID: {}", id);
    }

}
