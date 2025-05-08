package com.mervesaruhan.librarymanagementsystem.general;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowingDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;

import java.util.ArrayList;
import java.util.List;

public class BorrowingTestDataGenerator {

    public static Borrowing createBorrowing() {
        return new Borrowing(
                TestConstants.TEST_BORROWING_ID,
                TestConstants.TEST_BORROW_DATE,
                TestConstants.TEST_DUE_DATE,
                TestConstants.TEST_RETURN_DATE,
                TestConstants.TEST_BORROW_STATUS_BORROWED,
                BookTestDataGenerator.createBook(),
                UserTestDataGenerator.createUserLibrarian()
        );
    }

    public static BorrowingDto createBorrowingBookDto() {
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

    public static BorrowingDto createBorrowingOverDueBookDto() {
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

    public static List<Borrowing> createBorrowingList() {
        List<Borrowing> borrowingList = new ArrayList<>();
        Borrowing borrowedBook1 = createBorrowing();
        Borrowing borrowedBook2 = createBorrowing();
        borrowingList.add(borrowedBook1);
        borrowingList.add(borrowedBook2);
        return borrowingList;
    }

    public static List<BorrowingDto> createBorrowingDtoList() {
        List<BorrowingDto> borrowingList = new ArrayList<>();
        BorrowingDto borrowedBook1 = createBorrowingOverDueBookDto();
        BorrowingDto borrowedBook2 = createBorrowingOverDueBookDto();
        borrowingList.add(borrowedBook1);
        borrowingList.add(borrowedBook2);
        return borrowingList;
    }

}
