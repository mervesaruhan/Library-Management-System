package com.mervesaruhan.librarymanagementsystem.ReactivePrograming;

import com.mervesaruhan.librarymanagementsystem.repository.BookRepository;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookReactiveService {

    private final List<BookAvailabilityDto> bookStorage = new ArrayList<>();
    private final BookRepository bookRepository;
    private final LogHelper logHelper;

    @PostConstruct
    public List<BookAvailabilityDto> loadBooksIntoStorage() {
        final List<BookAvailabilityDto> bookDtoList = bookRepository.findAll().stream()
                .map(book -> new BookAvailabilityDto(book.getId(), book.getTitle(), book.getInventoryCount()))
                .toList();

        bookStorage.addAll(bookDtoList);
        return bookStorage;
    }

    public Flux<List<BookAvailabilityDto>> getAllBooks() {
        logHelper.info("getAllBooks");

        return Flux.interval(Duration.ofSeconds(5))
                .map(i -> {
                    return new ArrayList<>(bookStorage);
                });
    }

    public void updateInventoryCountById(Long id, Integer count) {
        logHelper.info("Updating inventory count by count={}", count);
        bookStorage.stream()
                .filter(book -> book.id().equals(id))
                .findFirst()
                .ifPresent(book -> {
                    bookStorage.remove(book);
                    bookStorage.add(new BookAvailabilityDto(book.id(), book.title(), count));
                });
        logHelper.info("Updating inventory count by count={}", count);
    }
}