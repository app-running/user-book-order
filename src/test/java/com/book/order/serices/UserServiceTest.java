package com.book.order.serices;

import com.book.order.converter.UserConvert;
import com.book.order.dto.UserRequest;
import com.book.order.entity.User;
import com.book.order.exception.UserBookOrderException;
import com.book.order.exception.ErrorResponse;
import com.book.order.repository.UserRepository;
import com.book.order.service.impl.UserServiceImpl;
import com.book.order.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.book.order.utils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConvert convert;

    @InjectMocks
    private UserServiceImpl usersServiceImpl;

    @Test
    void test_successful_add_book(){
        when(convert.toEntity(any(UserRequest.class))).thenCallRealMethod();
        when(convert.fromEntity(any(User.class))).thenCallRealMethod();
        when(userRepository.save(any(User.class))).thenReturn(USER_ENTITY);

        var result = usersServiceImpl.addUser(USER_REQUEST);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test", result.getName());
    }


    @Test
    void test_successful_find_user(){

        var userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(USER_ENTITY));

        var result = usersServiceImpl.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("test", result.getName());
    }

    @Test
    void test_fail_find_user(){

        var userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserBookOrderException exception = assertThrows(UserBookOrderException.class, () ->
            usersServiceImpl.findById(userId)
       );

        ErrorResponse errorResponse = Utils.convertToObject(exception.getMessage(), ErrorResponse.class);

        assertEquals("Not Found", errorResponse.getStatus());
        assertEquals(404, errorResponse.getStatusCode());
        assertEquals("User id : 1 not found", errorResponse.getErrorMessage());

    }
}
