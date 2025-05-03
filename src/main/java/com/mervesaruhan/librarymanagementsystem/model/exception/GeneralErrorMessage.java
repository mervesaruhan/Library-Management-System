package com.mervesaruhan.librarymanagementsystem.model.exception;



public enum GeneralErrorMessage implements BaseErrorMessage {
    BAD_REQUEST("Invalid request."),
    INTERNAL_ERROR("An internal server error occurred."),
    ILLEGAL_STATE("Invalid operation state.");

    private final String message;

    GeneralErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}