package com.book.order.itests;

import com.book.order.dto.BookRequest;
import com.book.order.dto.BookResponse;
import com.book.order.utils.Path;
import com.book.order.converter.BookConvert;
import com.book.order.repository.BookRepository;
import com.book.order.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.book.order.utils.Path.BOOK_PATH;
import static com.book.order.utils.Utils.BOOK_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void test_Successful_Add_Book(){

        BookResponse bookResponse = restTemplate
               .postForObject(Path.ADD_BOOK_PATH, BOOK_REQUEST, BookResponse.class);

        assertNotNull(bookResponse);
        assertEquals(bookResponse.getTitle(), BOOK_REQUEST.getTitle());
        assertEquals(bookResponse.getQuantity(), BOOK_REQUEST.getQuantity());
    }


    @Test
    void test_successful_get_all_book() throws Exception{

        var booksTitle = List.of("book 1", "book 2", "book 3");

        booksTitle.forEach(title -> {
            BookRequest bookRequest = BookRequest.builder().title(title).quantity(1).build();
                    restTemplate.postForObject(Path.ADD_BOOK_PATH, bookRequest, BookResponse.class);
                }
        );

        String json = restTemplate.getForObject(BOOK_PATH, String.class);
        var books =  Utils.convertToPageResponseContent(json, BookResponse.class);


        assertNotNull(books);
        assertEquals(books.size(), booksTitle.size());
        assertEquals(booksTitle, books.stream().map(BookResponse:: getTitle).toList());
    }


}
