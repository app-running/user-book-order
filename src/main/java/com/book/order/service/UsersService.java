package com.book.order.service;


import com.book.order.dto.UserRequest;
import com.book.order.dto.UserResponse;
import com.book.order.entity.User;
import org.springframework.data.domain.Page;


public interface UsersService {

    User findById(Long id);

    UserResponse addUser(UserRequest userRequest);

    Page<UserResponse> getAllUser(int page, int perPage);

}
