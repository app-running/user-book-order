package com.book.order.service.impl;

import com.book.order.converter.BookConvert;
import com.book.order.dto.BookRequest;
import com.book.order.dto.BookResponse;
import com.book.order.entity.Book;
import com.book.order.exception.UserBookOrderException;
import com.book.order.repository.BookRepository;
import com.book.order.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.book.order.exception.UserBookOrderException.customMessage;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookConvert convert;

    @Override
    public BookResponse addBook(BookRequest bookRequest) {
        Book newBook = convert.toEntity(bookRequest);
        Book createdBook = bookRepository.save(newBook);

        return convert.fromEntity(createdBook);
    }

    @Override
    public Page<BookResponse> getBooks(int page, int perPage) {

        Pageable pageable = PageRequest.of(page, perPage);
        Page<Book> bookOrderList = bookRepository.findAll(pageable);

        return bookOrderList.map(convert::fromEntity);
    }

    @Override
    public  Book findById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new UserBookOrderException(customMessage(HttpStatus.NOT_FOUND,
                        String.format("Book id : %s not found", bookId)
                )));
    }

    @Override
    public Book updateBook(Book book) {
       return bookRepository.save(book);
    }
}
