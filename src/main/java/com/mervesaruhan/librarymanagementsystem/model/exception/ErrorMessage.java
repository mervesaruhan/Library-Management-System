package com.mervesaruhan.librarymanagementsystem.model.exception;

public enum ErrorMessage implements BaseErrorMessage {
    USER_NOT_FOUND("User not found."),
    BOOK_NOT_FOUND("Book not found."),
    BOOK_ALREADY_BORROWED("Book has already been borrowed."),
    INVENTORY_EMPTY("Book inventory is empty."),
    USER_INACTIVE("User is not active."),
    ISBN_ALREADY_EXISTS("ISBN is already registered."),
    USER_STILL_ACTIVE("User cannot be deleted because they are still active."),
    ILLEGAL_ARGUMENT("Invalid argument provided.");


    private final String message;

    ErrorMessage(String message) {
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

