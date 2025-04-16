package com.book.order.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

public class BookOrderException extends RuntimeException {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public BookOrderException(String message) {
        super(message);
    }

    @SneakyThrows
    public static String customMessage(final HttpStatus status, final String errorMessage) {

        final ErrorResponse err =  ErrorResponse.builder()
                .status(status.getReasonPhrase())
                .statusCode(status.value())
                .errorMessage(errorMessage)
                .build();

        return objectMapper.writeValueAsString(err);
    }
}