package com.mervesaruhan.librarymanagementsystem.model.exception;

import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidBookIdException;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidUserIdException;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
@ControllerAdvice
@RequiredArgsConstructor
public class GeneralControllerAdvice extends ResponseEntityExceptionHandler {

    private final LogHelper logHelper;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        List<Map<String, String>> errorList = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> Map.of(fieldError.getField(), Objects.requireNonNull(fieldError.getDefaultMessage())))
                .toList();

        GeneralMessagesForValidations errorBody = new GeneralMessagesForValidations(
                LocalDateTime.now(),
                "Validation failed",
                request.getDescription(false),
                errorList
        );

        return new ResponseEntity<>(RestResponse.error(errorBody), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        String description = request.getDescription(false);
        var error = new GeneralErrorMessages(LocalDateTime.now(), ex.getMessage(), description);
        logHelper.error("BusinessException occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(RestResponse.error(error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<Object> handleInvalidUserId(InvalidUserIdException ex, WebRequest request) {
        var error = new GeneralErrorMessages(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        logHelper.error("InvalidUserIdException: {}", ex.getMessage());
        return new ResponseEntity<>(RestResponse.error(error), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidBookIdException.class)
    public ResponseEntity<Object> handleInvalidBookId(InvalidBookIdException ex, WebRequest request) {
        var error = new GeneralErrorMessages(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        logHelper.error("InvalidBookIdException: {}", ex.getMessage());
        return new ResponseEntity<>(RestResponse.error(error), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        String message = ex.getMessage() != null
                ? ex.getMessage()
                : GeneralErrorMessage.BAD_REQUEST.getMessage();

        var errorBody = new GeneralMessagesForValidations(
                LocalDateTime.now(),
                message,
                request.getDescription(false),
                null
        );
        logHelper.warn("EntityNotFoundException: {}", ex.getMessage());
        return new ResponseEntity<>(RestResponse.error(errorBody), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        String message = ex.getMessage() != null
                ? ex.getMessage()
                : ErrorMessage.ILLEGAL_ARGUMENT.getMessage();

        var errorBody = new GeneralMessagesForValidations(
                LocalDateTime.now(),
                message,
                request.getDescription(false),
                null
        );
        logHelper.error("IllegalArgumentException: {}", ex.getMessage());
        return new ResponseEntity<>(RestResponse.error(errorBody), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalState(IllegalStateException ex, WebRequest request) {

        String message = ex.getMessage() != null
                ? ex.getMessage()
                : GeneralErrorMessage.ILLEGAL_STATE.getMessage();

        var errorBody = new GeneralMessagesForValidations(
                LocalDateTime.now(),
                message,
                request.getDescription(false),
                null
        );
        logHelper.error("IllegalStateException: {}", ex.getMessage());
        return new ResponseEntity<>(RestResponse.error(errorBody), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationDenied(AuthorizationDeniedException ex, WebRequest request) {
        logHelper.error("AuthorizationDeniedException: {}", ex.getMessage());
        return new ResponseEntity<>(
                RestResponse.errorAuth(GeneralErrorMessage.ACCESS_DENIED, HttpStatus.FORBIDDEN),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        logHelper.error("AccessDeniedException: {}", ex.getMessage());
        return new ResponseEntity<>(
                RestResponse.errorAuth(GeneralErrorMessage.ACCESS_DENIED, HttpStatus.FORBIDDEN),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        var error = new GeneralErrorMessages(LocalDateTime.now(), GeneralErrorMessage.INTERNAL_ERROR.getMessage(), request.getDescription(false));
        logHelper.error("Unhandled Exception: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(RestResponse.error(error), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}