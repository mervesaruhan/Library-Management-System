package com.mervesaruhan.librarymanagementsystem.exception;



public enum GeneralErrorMessage implements BaseErrorMessage {
    BAD_REQUEST("Invalid request."),
    INTERNAL_ERROR("An internal server error occurred."),
    ILLEGAL_STATE("Invalid operation state."),
    UNAUTHORIZED("You need to login first."),
    ACCESS_DENIED("You do not have permission to perform this action.");

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