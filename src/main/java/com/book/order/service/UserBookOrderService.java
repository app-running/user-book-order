package com.book.order.service;

import com.book.order.dto.UserBookOrderRequest;
import com.book.order.dto.UserBookOrderResponse;
import org.springframework.data.domain.Page;

public interface UserBookOrderService {

  UserBookOrderResponse orderBorrowBook(UserBookOrderRequest bookOrderRequest);

  UserBookOrderResponse orderReturnBook(UserBookOrderRequest bookOrderRequest);

  Page<UserBookOrderResponse> userBorrowBookOrderList(Long user, int page, int perPage);

  Page<UserBookOrderResponse> userReturnBookOrderList(Long user, int page, int perPage);

}
