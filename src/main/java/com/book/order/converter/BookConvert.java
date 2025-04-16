package com.book.order.converter;

import com.book.order.dto.BookRequest;
import com.book.order.entity.Book;
import com.book.order.dto.BookResponse;
import org.springframework.stereotype.Component;

@Component
public class BookConvert {

    public BookResponse fromEntity(Book book){
        return  BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .quantity(book.getQuantity())
                .build();

    }
    public Book toEntity(BookRequest book){
        return  Book.builder()
                .title(book.getTitle())
                .quantity(book.getQuantity())
                .build();

    }
}
