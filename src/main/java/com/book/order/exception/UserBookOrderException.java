package com.book.order.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

public class UserBookOrderException extends RuntimeException {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public UserBookOrderException(String message) {
        super(message);
    }

    @SneakyThrows
    public static String customMessage(final HttpStatus status, final String errorMessage) {

        final ErrorResponse errorResponse =  ErrorResponse.builder()
                .status(status.getReasonPhrase())
                .statusCode(status.value())
                .errorMessage(errorMessage)
                .build();

        return objectMapper.writeValueAsString(errorResponse);
    }
}