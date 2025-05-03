package com.mervesaruhan.librarymanagementsystem.model.exception;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
//@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BusinessException extends RuntimeException {
    private final BaseErrorMessage baseErrorMessage;

    public BusinessException(BaseErrorMessage baseErrorMessage) {
        super(baseErrorMessage.getMessage());
        this.baseErrorMessage = baseErrorMessage;
    }

}