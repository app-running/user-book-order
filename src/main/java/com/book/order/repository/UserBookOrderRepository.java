package com.book.order.repository;

import com.book.order.entity.UsersBookOrder;
import com.book.order.enumeration.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBookOrderRepository extends JpaRepository<UsersBookOrder, Long> {

    Page<UsersBookOrder> findByUserIdAndStatusType(Long userId, Pageable pageable, StatusType type);

    Page<UsersBookOrder> findByStatusType(Pageable pageable, StatusType type);

    Optional<UsersBookOrder> findByUserIdAndBookIdAndStatusType(Long userId, Long bookId, StatusType statusType);

}
