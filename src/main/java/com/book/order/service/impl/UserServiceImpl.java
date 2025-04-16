package com.book.order.service.impl;

import com.book.order.converter.UserConvert;
import com.book.order.dto.UserRequest;
import com.book.order.dto.UserResponse;
import com.book.order.entity.User;
import com.book.order.exception.UserBookOrderException;
import com.book.order.repository.UserRepository;
import com.book.order.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.book.order.exception.UserBookOrderException.customMessage;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UsersService {

    private final UserRepository userRepository;

    private final UserConvert convert;

    @Override
    public UserResponse addUser(UserRequest userRequest) {

        User user = convert.toEntity(userRequest);

        User result = userRepository.save(user);
        return convert.fromEntity(result);
    }

    @Override
    public Page<UserResponse> getAllUser(int page, int perPage) {

        Pageable pageable = PageRequest.of(page, perPage);
        Page<User> userList = userRepository.findAll(pageable);

        return userList.map(convert::fromEntity);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserBookOrderException(customMessage(HttpStatus.NOT_FOUND,
                        String.format("User id : %s not found", userId)
                )));
    }
}
