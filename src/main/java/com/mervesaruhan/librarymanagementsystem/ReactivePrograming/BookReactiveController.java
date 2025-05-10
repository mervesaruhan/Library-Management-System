package com.mervesaruhan.librarymanagementsystem.ReactivePrograming;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booksAvailability")
public class BookReactiveController {

    private final BookReactiveService reactiveService;

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<BookAvailabilityDto>> getAllBooks() {
        return reactiveService.getAllBooks();
    }
}
