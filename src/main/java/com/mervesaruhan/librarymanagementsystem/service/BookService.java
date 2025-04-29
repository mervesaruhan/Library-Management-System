package com.mervesaruhan.librarymanagementsystem.service;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookAvailabilityUpdateDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;
import com.mervesaruhan.librarymanagementsystem.model.mapper.BookMapper;
import com.mervesaruhan.librarymanagementsystem.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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


    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream().map(bookMapper::toBookDto).collect(Collectors.toList());
    }

    public Page<BookDto> findAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toBookDto);
    }


    public BookDto findById(Long id){
        return bookRepository.findById(id).map(bookMapper::toBookDto)
                .orElseThrow(() -> new EntityNotFoundException("Girilen id'de kitap nulunamdı. id: " + id));
    }


    public Page<BookDto> searchBooks(String keyword, Pageable pageable, String field) {
        Page<Book> books;

        switch (field.toLowerCase()) {
            case "title":
                books = bookRepository.findByTitleContainingIgnoreCase(keyword, pageable);
                break;
            case "author":
                books = bookRepository.findByAuthorContainingIgnoreCase(keyword, pageable);
                break;
            case "isbn":
                books = bookRepository.findByIsbnContainingIgnoreCase(keyword, pageable);
            break;
            case "genre":
                books = bookRepository.findByGenreContainingIgnoreCase(keyword, pageable);
                break;
            default:
                throw new IllegalArgumentException("Geçersiz arama alanı: " + field);
        }
        return books.map(bookMapper::toBookDto);
    }




    public BookDto updateBook(BookUpdateRequestDto updateRequestDto, Long id){
        Book book= bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Girilen id'de kitap bulunamadı. id:" + id));

        //Başka bir kitapta bu isbn ar mı kontrolü
        if (bookRepository.existsByIsbnAndIdNot(updateRequestDto.isbn(), id)) {
            throw new IllegalArgumentException("Bu ISBN numarasıyla başka bir kitap zaten kayıtlı.");
        }

        book.setIsbn(updateRequestDto.isbn());
        book.setAuthor(updateRequestDto.author());
        book.setTitle(updateRequestDto.title());
        book.setPublisher(updateRequestDto.publisher());
        book.setPublishedDate(updateRequestDto.publishedDate());
        book.setGenre(updateRequestDto.genre());
        book.setAvailability(updateRequestDto.availability());
        book.setDescription(updateRequestDto.description());
        book.setPageCount(updateRequestDto.pageCount());

        bookRepository.save(book);
        return bookMapper.toBookDto(book);

    }


    public BookDto updateBookByAvailability(BookAvailabilityUpdateDto availabilityUpdateDto, Long id){
        Book book= bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Girilen id'de kitap bulunamadı. id:" + id));
        book.setAvailability(availabilityUpdateDto.availability());
        bookRepository.save(book);
        return bookMapper.toBookDto(book);
    }


    public void deleteBook(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Girilen id'de kitap bulunamadı. id: " + id));;
        bookRepository.delete(book);
    }

}
