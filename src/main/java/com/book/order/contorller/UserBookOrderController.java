package com.book.order.contorller;

import com.book.order.dto.UserBookOrderRequest;
import com.book.order.dto.UserBookOrderResponse;
import com.book.order.service.UserBookOrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserBookOrderController {

    private final UserBookOrderService bookOrderService;

    @GetMapping("/list/borrow-book")
    public ResponseEntity<List<UserBookOrderResponse>> getBorrowBooks(
         @RequestParam(required = false) Long user,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "100", name = "perPage") int perPage) {

        var response = bookOrderService.userBorrowBookOrderList(user, page, perPage).getContent();

        return  ResponseEntity.ok(response);
    }

    @GetMapping("/list/return-book")
    public ResponseEntity<List<UserBookOrderResponse>> getReturnBookList(
            @RequestParam(required = false) Long user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100", name = "perPage") int perPage){

        var response = bookOrderService.userReturnBookOrderList(user, page, perPage).getContent();

        return  ResponseEntity.ok(response);
    }

    @PostMapping("/borrow-book")
    public ResponseEntity<UserBookOrderResponse> borrowBook(
            @RequestBody @Valid UserBookOrderRequest bookOrderRequest) {

        return  ResponseEntity.ok(bookOrderService.orderBorrowBook(bookOrderRequest));
    }

    @PostMapping("/return-book")
    public ResponseEntity<UserBookOrderResponse> returnBook(
            @RequestBody @Valid UserBookOrderRequest bookOrderRequest) {

        return  ResponseEntity.ok(bookOrderService.orderReturnBook(bookOrderRequest));
    }
}
