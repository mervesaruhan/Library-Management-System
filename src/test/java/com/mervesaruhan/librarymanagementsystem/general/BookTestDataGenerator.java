package com.mervesaruhan.librarymanagementsystem.general;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowedBookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class BookTestDataGenerator {

    public static Book createBook() {
        Book book = new Book();
        book.setId(TestConstants.TEST_BOOK_ID);
        book.setTitle(TestConstants.TEST_BOOK_TITLE);
        book.setAuthor(TestConstants.TEST_BOOK_AUTHOR);
        book.setIsbn(TestConstants.TEST_BOOK_ISBN);
        book.setPublisher(TestConstants.TEST_BOOK_PUBLISHER);
        book.setPublishedDate(TestConstants.TEST_BOOK_PUBLISHED_DATE);
        book.setGenre(TestConstants.TEST_BOOK_GENRE);
        book.setDescription(TestConstants.TEST_BOOK_DESCRIPTION);
        book.setPageCount(TestConstants.TEST_BOOK_PAGE_COUNT);
        book.setInventoryCount(TestConstants.TEST_BOOK_INVENTORY_COUNT);
        return book;
    }

    public static BookDto createBookDto() {
        return new BookDto(
                TestConstants.TEST_BOOK_ID,
                TestConstants.TEST_BOOK_TITLE,
                TestConstants.TEST_BOOK_AUTHOR,
                TestConstants.TEST_BOOK_ISBN,
                TestConstants.TEST_BOOK_PUBLISHED_DATE,
                TestConstants.TEST_BOOK_PUBLISHER,
                TestConstants.TEST_BOOK_GENRE,
                TestConstants.TEST_BOOK_DESCRIPTION,
                TestConstants.TEST_BOOK_PAGE_COUNT,
                TestConstants.TEST_BOOK_INVENTORY_COUNT,
                true
        );
    }

    public static BookSaveRequestDto createBookSaveRequestDto() {
        return new BookSaveRequestDto(
                TestConstants.TEST_BOOK_TITLE,
                TestConstants.TEST_BOOK_AUTHOR,
                TestConstants.TEST_BOOK_ISBN,
                TestConstants.TEST_BOOK_PUBLISHED_DATE,
                TestConstants.TEST_BOOK_PUBLISHER,
                TestConstants.TEST_BOOK_GENRE,
                TestConstants.TEST_BOOK_DESCRIPTION,
                TestConstants.TEST_BOOK_PAGE_COUNT,
                TestConstants.TEST_BOOK_INVENTORY_COUNT
        );
    }

    public static BookUpdateRequestDto createBookUpdateRequestDto() {
        return new BookUpdateRequestDto(
                TestConstants.TEST_BOOK_TITLE,
                TestConstants.TEST_BOOK_AUTHOR,
                TestConstants.TEST_BOOK_ISBN,
                TestConstants.TEST_BOOK_PUBLISHED_DATE,
                TestConstants.TEST_BOOK_PUBLISHER,
                TestConstants.TEST_BOOK_GENRE,
                TestConstants.TEST_BOOK_DESCRIPTION,
                TestConstants.TEST_BOOK_PAGE_COUNT,
                TestConstants.TEST_BOOK_INVENTORY_COUNT
        );
    }

    public static BorrowedBookDto createBorrowedBookDto() {
        return new BorrowedBookDto(
                TestConstants.TEST_BOOK_ID,
                TestConstants.TEST_BOOK_TITLE,
                TestConstants.TEST_BOOK_AUTHOR
        );
    }

    public static List<BorrowedBookDto> createBorrowedBookDtoList() {
        List<BorrowedBookDto> borrowedBookDtoList = new ArrayList<>();
        BorrowedBookDto borrowedBook1 = createBorrowedBookDto();
        BorrowedBookDto borrowedBook2 = createBorrowedBookDto();
        borrowedBookDtoList.add(borrowedBook1);
        borrowedBookDtoList.add(borrowedBook2);
        return borrowedBookDtoList;
    }



}
