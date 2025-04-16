package com.book.order.serices;

import com.book.order.converter.BookConvert;
import com.book.order.dto.BookRequest;
import com.book.order.entity.Book;
import com.book.order.exception.BookOrderException;
import com.book.order.exception.ErrorResponse;
import com.book.order.repository.BookRepository;
import com.book.order.service.impl.BookServiceImpl;
import com.book.order.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.book.order.utils.Utils.BOOK_ENTITY;
import static com.book.order.utils.Utils.BOOK_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookConvert convert;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Test
    void testSuccessfulAddBook(){
        when(convert.toEntity(any(BookRequest.class))).thenCallRealMethod();
        when(convert.fromEntity(any(Book.class))).thenCallRealMethod();
        when(bookRepository.save(any(Book.class))).thenReturn(BOOK_ENTITY);

        var result = bookServiceImpl.addBook(BOOK_REQUEST);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test", result.getTitle());
    }

    @Test
    void testSuccessfulFindBookById(){

        var bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(BOOK_ENTITY));

        var result = bookServiceImpl.findById(bookId);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("test", result.getTitle());
    }

    @Test
    void testSuccessFindBooks(){

        var page = 0;
        var perPage = 100;

        var bookList  = List.of(BOOK_ENTITY);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Book> pageBookResponse = new PageImpl<>(bookList, pageRequest, bookList.size());

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(pageBookResponse);

        when(convert.fromEntity(any(Book.class))).thenCallRealMethod();

        var result = bookServiceImpl.getBooks(page,perPage);

        assertNotNull(result);
        assertEquals(result.getContent().size(), 1);
        assertEquals(result.getContent().getFirst().getTitle(), "test");
    }

    @Test
    void testFailFindBookById(){

        var bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BookOrderException exception = assertThrows(BookOrderException.class, () ->
            bookServiceImpl.findById(bookId)
        );

        ErrorResponse errorResponse = Utils.convertToObject(exception.getMessage(), ErrorResponse.class);

        assertEquals("Not Found", errorResponse.getStatus());
        assertEquals(404, errorResponse.getStatusCode());
        assertEquals("Book id : 1 not found", errorResponse.getErrorMessage());

    }
}
