package com.mervesaruhan.librarymanagementsystem.general;

import com.mervesaruhan.librarymanagementsystem.model.enums.BorrowingStatusEnum;
import com.mervesaruhan.librarymanagementsystem.model.enums.RoleEnum;

import java.time.LocalDate;

public class TestConstants {

    //BOOK

    public static final Long TEST_BOOK_ID = 1L;
    public static final String TEST_BOOK_TITLE = "Book Title";
    public static final String TEST_BOOK_AUTHOR = "Test Author";
    public static final String TEST_BOOK_ISBN = "Test 11111111111";
    public static final String TEST_BOOK_PUBLISHER = "Test Publisher";
    public static final LocalDate TEST_BOOK_PUBLISHED_DATE = LocalDate.of(1996, 1, 1);
    public static final String TEST_BOOK_GENRE = "Test Genre";
    public static final String TEST_BOOK_DESCRIPTION = "Test Description";
    public static final Integer TEST_BOOK_PAGE_COUNT = 350;
    public static final Integer TEST_BOOK_INVENTORY_COUNT = 50;

    //USER
    public static final Long TEST_USER_ID = 1L;
    public static final String TEST_USER_NAME = "Test User";
    public static final String TEST_UPDATED_USER_NAME = "Updated Test User";
    public static final String TEST_USER_SURNAME = "Test Surname";
    public static final String TEST_USER_FULLNAME = TEST_USER_NAME + " " + TEST_USER_SURNAME;
    public static final String TEST_UPDATED_USER_FULLNAME = TEST_UPDATED_USER_NAME + " " + TEST_USER_SURNAME;
    public static final String TEST_USER_EMAIL = "testemail@gmail.com";
    public static final String TEST_USER_PHONE = "05556667788";
    public static final String TEST_USER_USERNAME = "Test Username";
    public static final String TEST_USER_PASSWORD = "Test Password";
    public static final String TEST_USER_PASSWORD_NEW = "Test Password New";
    public static final RoleEnum TEST_ROLE_PATRON = RoleEnum.ROLE_PATRON;
    public static final RoleEnum TEST_ROLE_LIBRARIAN = RoleEnum.ROLE_LIBRARIAN;
    public static final Boolean TEST_IS_ACTIVE = true;
    public static final Boolean TEST_IS_INACTIVE = false;

    //BORROWING
    public static final Long TEST_BORROWING_ID = 1L;
    public static final LocalDate TEST_BORROW_DATE = LocalDate.now();
    public static final LocalDate TEST_DUE_DATE = LocalDate.now().plusDays(14);
    public static final LocalDate TEST_RETURN_DATE = LocalDate.now().plusDays(10);
    public static final BorrowingStatusEnum TEST_BORROW_STATUS_BORROWED = BorrowingStatusEnum.BORROWED;
    public static final BorrowingStatusEnum TEST_BORROW_STATUS_RETURNED = BorrowingStatusEnum.RETURNED;
    public static final BorrowingStatusEnum TEST_BORROW_STATUS_OVERDUE = BorrowingStatusEnum.OVERDUE;


}
