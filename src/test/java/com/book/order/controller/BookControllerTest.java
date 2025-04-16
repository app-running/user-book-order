package com.book.order.controller;

import com.book.order.utils.Utils;
import com.book.order.contorller.BookController;
import com.book.order.dto.BookResponse;
import com.book.order.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.book.order.utils.Utils.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {


    private MockMvc mockMvc;

    @Mock
    private BookService bookServiceș;


    @BeforeEach
    public void setUp() {

        BookController bookController =
                new BookController(bookServiceș);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(bookController)
                .build();
    }

    @Test
    void testSuccessfulAddNewBook() throws Exception {

        when(bookServiceș.addBook(BOOK_REQUEST)).thenReturn(BOOK_RESPONSE);
        this.mockMvc.perform(post("/book/addBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.convertToSting(BOOK_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.convertToSting(BOOK_RESPONSE)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testSuccessfulGetBooks() throws Exception {

        var page = 0;
        var perPage = 100;

        var bookList  = List.of(BOOK_RESPONSE);

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<BookResponse> pageBookResponse = new PageImpl<>(bookList, pageRequest, bookList.size());

        when(bookServiceș.getBooks(page, perPage)).thenReturn(pageBookResponse);

        this.mockMvc.perform(get("/book")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.convertToSting(bookList)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


}
