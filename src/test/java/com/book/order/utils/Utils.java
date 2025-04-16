package com.book.order.utils;

import com.book.order.dto.*;
import com.book.order.entity.Book;
import com.book.order.entity.User;
import com.book.order.entity.UsersBookOrder;
import com.book.order.enumeration.StatusType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public final class Utils {

     private final static ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    public static String convertToSting(final Object o) {

        return MAPPER.writeValueAsString(o);
    }


    @SneakyThrows
    public static <T> T convertToObject(final String json, Class<T> clazz) {
        return MAPPER.readValue(json, clazz);
    }

    public static final UserRequest USER_REQUEST = UserRequest
            .builder()
            .name("Test")
            .build();

    public static final UserResponse USER_RESPONSE = UserResponse.builder()
            .id(1L)
            .name("test")
            .build();

    public static final User USER_ENTITY = User.builder()
            .id(1L)
            .name("test")
            .build();

    public static final BookRequest BOOK_REQUEST = BookRequest
            .builder()
            .title("Test")
            .quantity(2)
            .build();

    public static final BookResponse BOOK_RESPONSE = BookResponse
            .builder()
            .id(1L)
            .title("Test")
            .quantity(2)
            .build();

    public static final Book BOOK_ENTITY = Book.builder()
            .id(1L)
            .title("test")
            .quantity(1)
            .build();

    public static final UserBookOrderRequest BOOK_ORDER_REQUEST = UserBookOrderRequest
            .builder()
            .bookId(1L)
            .userId(1L)
            .build();

    public static final UserBookOrderResponse USER_BOOK_ORDER_RESPONSE = UserBookOrderResponse
            .builder()
            .bookId(1L)
            .userId(1L)
            .statusType(StatusType.BORROW)
            .build();

    public static final UsersBookOrder USER_BOOK_ORDER = UsersBookOrder
            .builder()
            .id(1L)
            .book(BOOK_ENTITY)
            .user(USER_ENTITY)
            .statusType(StatusType.BORROW)
            .build();

}
