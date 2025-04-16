package com.book.order.itests;

import com.book.order.dto.BookRequest;
import com.book.order.dto.BookResponse;
import com.book.order.utils.Path;
import com.book.order.converter.BookConvert;
import com.book.order.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.yaml")
@ActiveProfiles("test")
public class BookITests {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookConvert bookConvert;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void init() {
        bookRepository.deleteAll();
    }

    @Test
    void testSuccessAddBook(){

        var booksTitle = List.of("book 1", "book 2", "book 3");

        booksTitle.forEach(title -> {

            BookRequest bookRequest = BookRequest.builder().title(title).quantity(1).build();
                    restTemplate.postForObject(Path.ADD_BOOK_PATH, bookRequest, BookResponse.class);
                }
        );

        var books = Arrays.asList(restTemplate.getForObject(Path.BOOK_PATH, BookResponse[].class));

        assertEquals(books.size(), booksTitle.size());
        assertEquals(booksTitle, books.stream().map(BookResponse:: getTitle).toList());
    }



}
