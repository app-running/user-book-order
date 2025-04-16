package com.book.order.controller;

import com.book.order.contorller.UserController;
import com.book.order.service.UsersService;
import com.book.order.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static com.book.order.utils.Utils.USER_REQUEST;
import static com.book.order.utils.Utils.USER_RESPONSE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UsersService usersService;


    @BeforeEach
    public void setUp() {

        UserController userController =
                new UserController(usersService);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void test_successful_create_user() throws Exception {

        when(usersService.addUser(USER_REQUEST)).thenReturn(USER_RESPONSE);
        this.mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.convertToSting(USER_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.convertToSting(USER_RESPONSE)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
