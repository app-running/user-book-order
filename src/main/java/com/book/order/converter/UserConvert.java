package com.book.order.converter;

import com.book.order.dto.UserRequest;
import com.book.order.dto.UserResponse;
import com.book.order.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConvert {

    public UserResponse fromEntity(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public User toEntity(UserRequest user){
        return User.builder()
                .name(user.getName())
                .build();
    }
}
