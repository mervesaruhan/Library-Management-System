package com.mervesaruhan.librarymanagementsystem.service;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;
import com.mervesaruhan.librarymanagementsystem.model.enums.BookSearchField;
import com.mervesaruhan.librarymanagementsystem.model.mapper.BookMapper;
import com.mervesaruhan.librarymanagementsystem.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookDto saveBook(BookSaveRequestDto saveRequestDto){

        // ISBN benzersizlik kontrolü
        if (bookRepository.existsByIsbn(saveRequestDto.isbn())) {
            throw new IllegalArgumentException("Bu ISBN numarasıyla zaten bir kitap kayıtlı.");
        }

        Book book = bookMapper.toBookEntity(saveRequestDto);

        bookRepository.save(book);
        return bookMapper.toBookDto(book);
    }



    public Page<BookDto> findAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toBookDto);
    }


    public BookDto findById(Long id){
        return bookRepository.findById(id).map(bookMapper::toBookDto)
                .orElseThrow(() -> new EntityNotFoundException("Girilen id'de kitap nulunamdı. id: " + id));
    }


    public Page<BookDto> searchBooks(String keyword, Pageable pageable, BookSearchField field) {
        Page<Book> books = switch (field) {
            case TITLE -> bookRepository.findByTitleContainingIgnoreCase(keyword, pageable);
            case AUTHOR -> bookRepository.findByAuthorContainingIgnoreCase(keyword, pageable);
            case ISBN -> bookRepository.findByIsbnContainingIgnoreCase(keyword, pageable);
            case GENRE -> bookRepository.findByGenreContainingIgnoreCase(keyword, pageable);
        };

        return books.map(bookMapper::toBookDto);
    }



    public BookDto updateBook(BookUpdateRequestDto updateRequestDto, Long id){
        Book book= bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Girilen id'de kitap bulunamadı. id:" + id));

        //Başka bir kitapta bu isbn var mı kontrolü
        if (bookRepository.existsByIsbnAndIdNot(updateRequestDto.isbn(), id)) {
            throw new IllegalArgumentException("Bu ISBN numarasıyla başka bir kitap zaten kayıtlı.");
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
        return bookMapper.toBookDto(book);

    }


    public BookDto updateInventory(Long id, int inventoryCount){
        Book book = bookRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Girilen id'de kitap bulunamadı. id:" + id));
        book.setInventoryCount(inventoryCount);
        bookRepository.save(book);
        return bookMapper.toBookDto(book);
    }


    public Page<BookDto> getBooksByAvailability(int count, Pageable pageable){
        Page<Book> books = bookRepository.findBooksByInventoryCountGreaterThan(count, pageable);
        return books.map(bookMapper::toBookDto);
    }


    public void deleteBook(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Girilen id'de kitap bulunamadı. id: " + id));
        bookRepository.delete(book);
    }

}
