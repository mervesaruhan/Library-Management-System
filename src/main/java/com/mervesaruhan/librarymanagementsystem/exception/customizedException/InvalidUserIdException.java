package com.mervesaruhan.librarymanagementsystem.exception.customizedException;

import com.mervesaruhan.librarymanagementsystem.exception.BusinessException;
import com.mervesaruhan.librarymanagementsystem.exception.ErrorMessage;


public class InvalidUserIdException extends BusinessException {
    public InvalidUserIdException() {
        super(ErrorMessage.USER_NOT_FOUND);
    }
    public InvalidUserIdException(Long id) {
        super(() -> "User not found with id: " + id);
    }
}
