package com.book.order.entity;


import com.book.order.enumeration.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_books_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersBookOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusType statusType;

    @Column(name = "created_at")
    private LocalDate createdAt;

}
