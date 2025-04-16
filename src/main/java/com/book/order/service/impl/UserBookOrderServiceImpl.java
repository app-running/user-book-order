package com.book.order.service.impl;

import com.book.order.entity.*;
import com.book.order.converter.BookOrderConvert;
import com.book.order.dto.UserBookOrderRequest;
import com.book.order.dto.UserBookOrderResponse;
import com.book.order.enumeration.StatusType;
import com.book.order.exception.BookOrderException;
import com.book.order.repository.UserBookOrderRepository;
import com.book.order.service.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.book.order.exception.BookOrderException.customMessage;

@Service
@AllArgsConstructor
public class UserBookOrderServiceImpl implements UserBookOrderService {

    private final UserBookOrderRepository bookOrderRepository;

    private final UsersService usersService;

    private final BookService bookService;

    private final BookOrderConvert convert;

    @Override
    @Transactional
    public UserBookOrderResponse orderBorrowBook(UserBookOrderRequest bookOrderRequest) {

        User user = usersService.findById(bookOrderRequest.getUserId());
        Book book = bookService.findById(bookOrderRequest.getBookId());

        bookOrderRepository.findByUserIdAndBookIdAndStatusType(user.getId(), book.getId(), StatusType.BORROW)
                .ifPresent(p -> {
                    throw new BookOrderException(customMessage(HttpStatus.CONFLICT,
                            String.format("Book %s has already borrowed by this user id : %s ", book.getId(), user.getId())));
                });

        if (book.getQuantity() == 0) throw
                new BookOrderException(customMessage(HttpStatus.NOT_FOUND,
                        String.format("Book id:  %s with title: %s is not available", book.getId(), book.getTitle())));

        UsersBookOrder orderBook = convert.toNewEntity(user, book, StatusType.BORROW);

        book.setQuantity(book.getQuantity() - 1);
        bookService.updateBook(book);

        return convert.fromEntity(bookOrderRepository.save(orderBook));
    }

    @Override
    @Transactional
    public UserBookOrderResponse orderReturnBook(UserBookOrderRequest bookOrderRequest) {

        User user = usersService.findById(bookOrderRequest.getUserId());
        Book book = bookService.findById(bookOrderRequest.getBookId());

        UsersBookOrder bookOrder = bookOrderRepository.findByUserIdAndBookIdAndStatusType(user.getId(), book.getId(), StatusType.BORROW)
                .orElseThrow(() -> new BookOrderException(customMessage(HttpStatus.NOT_FOUND,
                        String.format("The book :%s is not currently borrowed by the user :%s", book.getId(), user.getId())
                )));

        bookOrder.setStatusType(StatusType.RETURN);

        book.setQuantity(book.getQuantity() + 1);
        bookService.updateBook(book);

        return convert.fromEntity(bookOrderRepository.save(bookOrder));
    }

    @Override
    public Page<UserBookOrderResponse> userBorrowBookOrderList(Long user, int page, int perPage) {
        return getListOfBookOrder(user, page, perPage, StatusType.BORROW);
    }

    @Override
    public Page<UserBookOrderResponse> userReturnBookOrderList(Long user, int page, int perPage) {
        return getListOfBookOrder(user, page, perPage, StatusType.RETURN);
    }

    //private Methods
    private Page<UserBookOrderResponse> getListOfBookOrder(Long user, int page, int perPage, StatusType type) {
        Pageable pageable = PageRequest.of(page, perPage);

        Page<UsersBookOrder> bookOrderList;

        if (user != null) {
            bookOrderList = bookOrderRepository.findByUserIdAndStatusType(user, pageable, type);
        } else {
            bookOrderList = bookOrderRepository.findByStatusType(pageable, type);
        }

        return bookOrderList.map(convert::fromEntity);
    }
}
