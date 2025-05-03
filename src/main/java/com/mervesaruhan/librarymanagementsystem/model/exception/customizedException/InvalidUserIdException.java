package com.mervesaruhan.librarymanagementsystem.model.exception.customizedException;

import com.mervesaruhan.librarymanagementsystem.model.exception.BusinessException;
import com.mervesaruhan.librarymanagementsystem.model.exception.ErrorMessage;


public class InvalidUserIdException extends BusinessException {
    public InvalidUserIdException() {
        super(ErrorMessage.USER_NOT_FOUND);
    }
    public InvalidUserIdException(Long id) {
        super(() -> "User not found with id: " + id);
    }
}
