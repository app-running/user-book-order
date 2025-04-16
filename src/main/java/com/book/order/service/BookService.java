package com.book.order.service;

import com.book.order.dto.BookRequest;
import com.book.order.dto.BookResponse;
import com.book.order.entity.Book;
import org.springframework.data.domain.Page;

public interface BookService {

   Book findById(Long bookId);

   Book updateBook(Book book);

   BookResponse addBook(BookRequest bookRequest);

   Page<BookResponse> getBooks(int page, int perPage);

}
