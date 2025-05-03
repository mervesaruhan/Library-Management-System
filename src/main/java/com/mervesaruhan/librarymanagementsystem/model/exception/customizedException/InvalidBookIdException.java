package com.mervesaruhan.librarymanagementsystem.model.exception.customizedException;

import com.mervesaruhan.librarymanagementsystem.model.exception.BusinessException;
import com.mervesaruhan.librarymanagementsystem.model.exception.ErrorMessage;

public class InvalidBookIdException extends BusinessException {
    public InvalidBookIdException() {
        super(ErrorMessage.BOOK_NOT_FOUND);
    }

    public InvalidBookIdException(Long id) {
        super(() -> "Book not found with id: " + id);
    }
}
