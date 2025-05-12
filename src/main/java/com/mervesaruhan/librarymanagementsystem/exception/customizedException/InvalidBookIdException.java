package com.mervesaruhan.librarymanagementsystem.exception.customizedException;

import com.mervesaruhan.librarymanagementsystem.exception.BusinessException;
import com.mervesaruhan.librarymanagementsystem.exception.ErrorMessage;

public class InvalidBookIdException extends BusinessException {
    public InvalidBookIdException() {
        super(ErrorMessage.BOOK_NOT_FOUND);
    }

    public InvalidBookIdException(Long id) {
        super(() -> "Book not found with id: " + id);
    }
}
