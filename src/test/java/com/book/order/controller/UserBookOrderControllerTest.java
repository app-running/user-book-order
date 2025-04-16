package com.book.order.controller;

import com.book.order.contorller.UserBookOrderController;
import com.book.order.dto.UserBookOrderResponse;
import com.book.order.exception.GlobalExceptionHandler;
import com.book.order.service.UserBookOrderService;
import com.book.order.utils.SerializationService;
import com.book.order.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.book.order.utils.Path.*;
import static com.book.order.utils.Utils.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
public class UserBookOrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserBookOrderService userBookOrderService;

    @Mock
    private SerializationService serializationService;

    @InjectMocks
    private UserBookOrderController userBookOrderController;

    @BeforeEach
    public void setUp() {

        this.mockMvc = standaloneSetup(userBookOrderController).setControllerAdvice(new GlobalExceptionHandler(serializationService)).build();

        UserBookOrderController userBookOrderController =
                new UserBookOrderController(userBookOrderService);

        this.mockMvc = standaloneSetup(userBookOrderController)
                .build();
    }

    @Test
    void test_successful_borrow_book() throws Exception{

        when(userBookOrderService.orderBorrowBook(BOOK_ORDER_REQUEST)).thenReturn(USER_BOOK_ORDER_RESPONSE);
        this.mockMvc.perform(post(BORROW_BOOK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.convertToSting(BOOK_ORDER_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.convertToSting(USER_BOOK_ORDER_RESPONSE)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void test_successful_return_book() throws Exception{

        when(userBookOrderService.orderReturnBook(BOOK_ORDER_REQUEST)).thenReturn(USER_BOOK_ORDER_RESPONSE);
        this.mockMvc.perform(post(RETURN_BOOK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.convertToSting(BOOK_ORDER_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.convertToSting(USER_BOOK_ORDER_RESPONSE)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void test_successful_list_of_borrowed_books_by_user() throws Exception{

        var page = 0;
        var perPage = 100;
        var userId = 1L;

        var userBookList = List.of(USER_BOOK_ORDER_RESPONSE);

        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<UserBookOrderResponse> userBookOrderResponse = new PageImpl<>(userBookList, pageRequest, userBookList.size());

        when(userBookOrderService.userBorrowBookOrderList(userId, page, perPage)).thenReturn(userBookOrderResponse);
        this.mockMvc.perform(get(LIST_BORROW_BOOK_PATH)
                        .param("user", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.convertToSting(BOOK_ORDER_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.convertToSting(userBookOrderResponse)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void test_successful_list_of_borrow_books() throws Exception{

        var page = 0;
        var perPage = 100;

        var userBookList = List.of(USER_BOOK_ORDER_RESPONSE);

        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<UserBookOrderResponse> userBookOrderResponse = new PageImpl<>(userBookList, pageRequest, userBookList.size());

        when(userBookOrderService.userBorrowBookOrderList(null, page, perPage)).thenReturn(userBookOrderResponse);
        this.mockMvc.perform(get(LIST_BORROW_BOOK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.convertToSting(BOOK_ORDER_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.convertToSting(userBookOrderResponse)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
