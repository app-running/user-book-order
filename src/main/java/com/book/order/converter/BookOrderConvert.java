package com.book.order.converter;


import com.book.order.dto.UserBookOrderResponse;
import com.book.order.entity.*;
import com.book.order.enumeration.StatusType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookOrderConvert {

    public UserBookOrderResponse fromEntity(UsersBookOrder bookOrder){

       return UserBookOrderResponse.builder()
               .userId(bookOrder.getUser().getId())
               .bookId(bookOrder.getBook().getId())
               .userName(bookOrder.getUser().getName())
               .title(bookOrder.getBook().getTitle())
               .createdAt(bookOrder.getCreatedAt())
               .statusType(bookOrder.getStatusType())
               .build();
    }

    public UsersBookOrder toNewEntity(User user, Book book, StatusType statusType){
      return UsersBookOrder.builder()
                .book(book)
                .user(user)
                .statusType(statusType)
                .createdAt(LocalDate.now())
                .build();
    }

}
