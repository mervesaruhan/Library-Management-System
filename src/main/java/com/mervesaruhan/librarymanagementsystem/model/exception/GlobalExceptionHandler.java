package com.mervesaruhan.librarymanagementsystem.model.exception;

import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidBookIdException;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidUserIdException;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Genel iş kuralları için
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<RestResponse<String>> handleBusinessException(BusinessException ex) {
        log.error("BusinessException occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .body(RestResponse.error(null, ex.getMessage()));
    }

    // Özel custom exception’lar
    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<RestResponse<String>> handleUserNotFound(InvalidUserIdException ex) {
        log.warn("InvalidUserIdException occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(null, ex.getMessage()));
    }

    @ExceptionHandler(InvalidBookIdException.class)
    public ResponseEntity<RestResponse<String>> handleBookNotFound(InvalidBookIdException ex) {
        log.warn("InvalidBookIdException occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(null, ex.getMessage()));
    }

    // JPA EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RestResponse<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn("EntityNotFoundException occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(null, ex.getMessage()));
    }

    // Illegal State
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<RestResponse<String>> handleIllegalStateException(IllegalStateException ex) {
        log.error("IllegalStateException occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(RestResponse.error(null, GeneralErrorMessage.ILLEGAL_STATE.getMessage()));
    }

    // Illegal Argument
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RestResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .body(RestResponse.error(null, ErrorMessage.ILLEGAL_ARGUMENT.getMessage()));
    }

    // Validation hataları (DTO seviyesinde)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Geçersiz veri.");
        log.warn("Validation error occurred: {}", errorMessage);

        return ResponseEntity.badRequest()
                .body(RestResponse.error(null, errorMessage));
    }

    // Tüm bilinmeyen hatalar
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<String>> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RestResponse.error(null, GeneralErrorMessage.INTERNAL_ERROR.getMessage()));
    }
}