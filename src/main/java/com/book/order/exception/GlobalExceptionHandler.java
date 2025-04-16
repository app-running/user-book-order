package com.book.order.exception;

import com.book.order.utils.SerializationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private final SerializationService serializationService;

    public GlobalExceptionHandler(SerializationService serializationService) {
        this.serializationService = serializationService;
    }

    @ExceptionHandler(UserBookOrderException.class)
    public ResponseEntity<ErrorResponse> handle(final UserBookOrderException bookOrderException) {

        final ErrorResponse errorResponse =
                serializationService.convertToObject(bookOrderException.getMessage(), ErrorResponse.class);

        return ResponseEntity
                .status(errorResponse.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleError(final MethodArgumentNotValidException ex) {

        var fieldsErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> Map.of(fieldError.getField(),
                        Objects.requireNonNull(fieldError.getDefaultMessage())))
                .toList();

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage(fieldsErrors.toString())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
