package com.book.order.dto;

import com.book.order.enumeration.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBookOrderResponse {
    private Long userId;
    private Long bookId;
    private String userName;
    private String title;
    private StatusType statusType;
    private LocalDate createdAt;
}
