package com.mervesaruhan.librarymanagementsystem.general;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowedBookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowingDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.UserDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.*;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserPasswordUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserRoleUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;
import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;

import java.util.ArrayList;
import java.util.List;

public class TestDataGenerator {

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

    public static BorrowedBookDto createBorrowedBookDto() {
        return new BorrowedBookDto(
                TestConstants.TEST_BOOK_ID,
                TestConstants.TEST_BOOK_TITLE,
                TestConstants.TEST_BOOK_AUTHOR
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

    public static Book createBook(){
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

    public static List<BorrowedBookDto> createBorrowedBookDtoList(){
        List<BorrowedBookDto> borrowedBookDtoList = new ArrayList<>();
        BorrowedBookDto borrowedBook1 = createBorrowedBookDto();
        BorrowedBookDto borrowedBook2 = createBorrowedBookDto();
        borrowedBookDtoList.add(borrowedBook1);
        borrowedBookDtoList.add(borrowedBook2);
        return borrowedBookDtoList;

    }

    //###################################################################################

    public static UserDto createUserDto() {
        return new UserDto(
                TestConstants.TEST_USER_ID,
                TestConstants.TEST_USER_FULLNAME,
                TestConstants.TEST_USER_EMAIL,
                TestConstants.TEST_USER_USERNAME,
                TestConstants.TEST_ROLE_LIBRARIAN,
                TestConstants.TEST_IS_ACTIVE,
                createBorrowedBookDtoList()
        );
    }


    public static UserSaveRequestDto createUserSaveLibrarianDto() {
        return new UserSaveRequestDto(

                TestConstants.TEST_USER_NAME,
                TestConstants.TEST_USER_SURNAME,
                TestConstants.TEST_USER_EMAIL,
                TestConstants.TEST_USER_PHONE,
                TestConstants.TEST_USER_USERNAME,
                TestConstants.TEST_USER_PASSWORD,
                TestConstants.TEST_ROLE_LIBRARIAN
        );
    }

    public static RegisterRequestDto createRegisterRequestDto() {
        return new RegisterRequestDto(

                TestConstants.TEST_USER_NAME,
                TestConstants.TEST_USER_SURNAME,
                TestConstants.TEST_USER_EMAIL,
                TestConstants.TEST_USER_PHONE,
                TestConstants.TEST_USER_USERNAME,
                TestConstants.TEST_USER_PASSWORD
        );
    }

    public static AuthRequest createAuthRequest() {
        return new AuthRequest(
                TestConstants.TEST_USER_USERNAME,
                TestConstants.TEST_USER_PASSWORD);
    }




    public static UserUpdateRequestDto userUpdateDto() {
        return new UserUpdateRequestDto(

                TestConstants.TEST_USER_NAME,
                TestConstants.TEST_USER_SURNAME,
                TestConstants.TEST_USER_EMAIL,
                TestConstants.TEST_USER_PHONE,
                TestConstants.TEST_USER_USERNAME
        );
    }

    public static UserRoleUpdateRequestDto roleUpdateDtoPatron() {
        return new UserRoleUpdateRequestDto(
                TestConstants.TEST_ROLE_PATRON
        );
    }
    public static UserRoleUpdateRequestDto roleUpdateDtoLibrarian() {
        return new UserRoleUpdateRequestDto(
                TestConstants.TEST_ROLE_LIBRARIAN
        );
    }
    public static UserPasswordUpdateRequestDto passwordUpdateDto(){
        return new UserPasswordUpdateRequestDto(
                TestConstants.TEST_USER_PASSWORD,
                TestConstants.TEST_USER_PASSWORD_NEW
        );
    }

    public static User createUserLibrarian(){
        User user = new User();
        user.setId(TestConstants.TEST_USER_ID);
        user.setName(TestConstants.TEST_USER_NAME);
        user.setSurname(TestConstants.TEST_USER_SURNAME);
        user.setEmail(TestConstants.TEST_USER_EMAIL);
        user.setPhone(TestConstants.TEST_USER_PHONE);
        user.setUsername(TestConstants.TEST_USER_USERNAME);
        user.setPassword(TestConstants.TEST_USER_PASSWORD);
        user.setRole(TestConstants.TEST_ROLE_LIBRARIAN);
        user.setActive(TestConstants.TEST_IS_ACTIVE);
        return user;
    }

    public static User createUserPatron(){
        User user = new User();
        user.setId(TestConstants.TEST_USER_ID);
        user.setName(TestConstants.TEST_USER_NAME);
        user.setSurname(TestConstants.TEST_USER_SURNAME);
        user.setEmail(TestConstants.TEST_USER_EMAIL);
        user.setPhone(TestConstants.TEST_USER_PHONE);
        user.setUsername(TestConstants.TEST_USER_USERNAME);
        user.setPassword(TestConstants.TEST_USER_PASSWORD);
        user.setRole(TestConstants.TEST_ROLE_PATRON);
        user.setActive(TestConstants.TEST_IS_ACTIVE);
        return user;
    }

//###################################################################################

    public static BorrowingDto createdBorrowingDtoBorrowed() {
        return new BorrowingDto(
                TestConstants.TEST_BORROWING_ID,
                TestConstants.TEST_USER_FULLNAME,
                TestConstants.TEST_USER_ID,
                TestConstants.TEST_BOOK_TITLE,
                TestConstants.TEST_BOOK_ID,
                TestConstants.TEST_BORROW_DATE,
                TestConstants.TEST_DUE_DATE,
                TestConstants.TEST_RETURN_DATE,
                TestConstants.TEST_BORROW_STATUS_BORROWED
        );
    }

    public static BorrowingDto createBorrowingDtoOverDue() {
        return new BorrowingDto(
                TestConstants.TEST_BORROWING_ID,
                TestConstants.TEST_USER_FULLNAME,
                TestConstants.TEST_USER_ID,
                TestConstants.TEST_BOOK_TITLE,
                TestConstants.TEST_BOOK_ID,
                TestConstants.TEST_BORROW_DATE,
                TestConstants.TEST_DUE_DATE,
                TestConstants.TEST_RETURN_DATE,
                TestConstants.TEST_BORROW_STATUS_OVERDUE
        );
    }

    public static BorrowingSaveRequestDto createBorrowingSaveRequestDto() {
        return new BorrowingSaveRequestDto(
                TestConstants.TEST_USER_ID,
                TestConstants.TEST_BOOK_ID
        );
    }

    public Borrowing createBorrowedBorrowing() {
        Borrowing borrowing = new Borrowing();
        borrowing.setId(TestConstants.TEST_BORROWING_ID);
        borrowing.setBorrowDate(TestConstants.TEST_BORROW_DATE);
        borrowing.setDueDate(TestConstants.TEST_DUE_DATE);
        borrowing.setReturnDate(TestConstants.TEST_RETURN_DATE);
        borrowing.setStatus(TestConstants.TEST_BORROW_STATUS_BORROWED);
        return borrowing;
    }

    public Borrowing createOverDueBorrowing() {
        Borrowing borrowing = new Borrowing();
        borrowing.setId(TestConstants.TEST_BORROWING_ID);
        borrowing.setBorrowDate(TestConstants.TEST_BORROW_DATE);
        borrowing.setDueDate(TestConstants.TEST_DUE_DATE);
        borrowing.setReturnDate(TestConstants.TEST_RETURN_DATE);
        borrowing.setStatus(TestConstants.TEST_BORROW_STATUS_OVERDUE);
        return borrowing;
    }

}
