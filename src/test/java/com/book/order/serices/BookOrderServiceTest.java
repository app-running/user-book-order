package com.book.order.serices;


import com.book.order.converter.BookOrderConvert;
import com.book.order.entity.Book;
import com.book.order.entity.User;
import com.book.order.entity.UsersBookOrder;
import com.book.order.enumeration.StatusType;
import com.book.order.exception.UserBookOrderException;
import com.book.order.exception.ErrorResponse;
import com.book.order.repository.UserBookOrderRepository;
import com.book.order.service.BookService;
import com.book.order.service.UsersService;
import com.book.order.service.impl.UserBookOrderServiceImpl;

import com.book.order.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static com.book.order.utils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookOrderServiceTest {

    @Mock
    private UserBookOrderRepository userBookOrderRepository;

    @Mock
    private UsersService usersService;

    @Mock
    private BookService bookService;

    @Mock
    private BookOrderConvert convert;

    @InjectMocks
    private UserBookOrderServiceImpl bookOrderServiceImpl;

    public void init(){

        when(usersService.findById(1L)).thenReturn(USER_ENTITY);
        when(bookService.findById(1L)).thenReturn(BOOK_ENTITY);
        when(bookService.updateBook(any(Book.class))).thenReturn(BOOK_ENTITY);

        when(convert.fromEntity(any(UsersBookOrder.class))).thenCallRealMethod();
    }

    @Test
    void  test_successful_user_borrow_book(){

        init();

        when(convert.toNewEntity(any(User.class),any(Book.class), any(StatusType.class))).thenCallRealMethod();
        when(userBookOrderRepository.findByUserIdAndBookIdAndStatusType(1L, 1L, StatusType.BORROW)).thenReturn(Optional.empty());
        when(userBookOrderRepository.save(any(UsersBookOrder.class))).thenReturn(USER_BOOK_ORDER);

        var result  = bookOrderServiceImpl.orderBorrowBook(BOOK_ORDER_REQUEST);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getBookId());
        assertEquals(StatusType.BORROW, result.getStatusType());

    }

    @Test
    void  test_successful_user_return_book(){

        init();

        when(userBookOrderRepository.findByUserIdAndBookIdAndStatusType(1L, 1L, StatusType.BORROW))
                .thenReturn(Optional.of(USER_BOOK_ORDER));
        when(userBookOrderRepository.save(any(UsersBookOrder.class))).thenReturn(USER_BOOK_ORDER);

        var result  = bookOrderServiceImpl.orderReturnBook(BOOK_ORDER_REQUEST);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getBookId());
        assertEquals(StatusType.RETURN, result.getStatusType());
    }

    @Test
    void  test_successful_list_user_borrow_book_by_user_id(){

        var page = 0;
        var perPage = 100;
        var userId = 1L;

        var  userBookOrderList = List.of(USER_BOOK_ORDER);

        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<UsersBookOrder> pageBookResponse = new PageImpl<>(userBookOrderList, pageRequest, userBookOrderList.size());


        when(userBookOrderRepository.findByUserIdAndStatusType(userId, pageRequest, StatusType.BORROW)).thenReturn(pageBookResponse);
        when(convert.fromEntity(any(UsersBookOrder.class))).thenCallRealMethod();

        var result  = bookOrderServiceImpl.userBorrowBookOrderList(userId, page, perPage);

        assertNotNull(result);
        assertEquals(result.getContent(), userBookOrderList.stream().map(convert :: fromEntity).toList());
    }

    @Test
    void  test_successful_list_user_borrow_book(){

        var page = 0;
        var perPage = 100;

        var  userBookOrderList = List.of(USER_BOOK_ORDER);

        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<UsersBookOrder> pageBookResponse = new PageImpl<>(userBookOrderList, pageRequest, userBookOrderList.size());

        when(userBookOrderRepository.findByStatusType(pageRequest, StatusType.BORROW)).thenReturn(pageBookResponse);
        when(convert.fromEntity(any(UsersBookOrder.class))).thenCallRealMethod();

        var result  = bookOrderServiceImpl.userBorrowBookOrderList(null, page, perPage);

        assertNotNull(result);
        assertEquals(result.getContent(), userBookOrderList.stream().map(convert :: fromEntity).toList());
    }

    @Test
    void  test_fail_borrow_book_twice_by_user(){

        when(usersService.findById(1L)).thenReturn(USER_ENTITY);
        when(bookService.findById(1L)).thenReturn(BOOK_ENTITY);

        when(userBookOrderRepository.findByUserIdAndBookIdAndStatusType(1L, 1L, StatusType.BORROW))
                .thenReturn(Optional.of(USER_BOOK_ORDER));

        UserBookOrderException exception = assertThrows(UserBookOrderException.class, () ->
            bookOrderServiceImpl.orderBorrowBook(BOOK_ORDER_REQUEST)
        );

        ErrorResponse errorResponse = Utils.convertToObject(exception.getMessage(), ErrorResponse.class);

        assertEquals("Conflict", errorResponse.getStatus());
        assertEquals(409, errorResponse.getStatusCode());
        assertEquals("Book 1 has already borrowed by this user id : 1 ", errorResponse.getErrorMessage());
    }
}