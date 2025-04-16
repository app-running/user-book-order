package com.book.order.itests;

import com.book.order.dto.*;
import com.book.order.entity.*;
import com.book.order.repository.*;
import com.book.order.utils.Path;
import com.book.order.converter.BookConvert;
import com.book.order.enumeration.StatusType;
import com.book.order.exception.ErrorResponse;
import com.book.order.utils.Utils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.yaml")
@ActiveProfiles("test")
public class BookOrderITests {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBookOrderRepository userBookOrderRepository;


    @Autowired
    private BookConvert bookConvert;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void init() {
        userBookOrderRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();


        List.of("title 1").forEach(title -> {
                    var book = Book.builder().title(title).quantity(1).build();
                    bookRepository.save(book);
                }
        );

        List.of("user 1", "user 2").forEach(name -> {
                    var user = User.builder().name(name).build();
                    userRepository.save(user);
                }
        );
    }

    @Test
    void test_Successful_Borrow_Book() throws Exception{

        final var books = fetchList(Path.BOOK_PATH, BookResponse.class);
        final var users = fetchList(Path.USER_PATH, UserResponse.class);

        final var userId = users.getFirst().getId();
        final var bookId = books.getFirst().getId();

        final var bookOrderRequest =
               UserBookOrderRequest.builder()
                       .userId(userId)
                       .bookId(bookId)
                       .build();

        final UserBookOrderResponse bookOrderResponse = userBookOrder(Path.BORROW_BOOK_PATH, bookOrderRequest);

        assertEquals(bookId, bookOrderResponse.getBookId());
        assertEquals(userId, bookOrderResponse.getUserId());
        assertEquals(StatusType.BORROW, bookOrderResponse.getStatusType());
    }

    @Test
    void test_Successful_Return_Book() throws Exception{

        var books = fetchList(Path.BOOK_PATH, BookResponse.class);
        var users = fetchList(Path.USER_PATH, UserResponse.class);

        var userId = users.getFirst().getId();
        var bookId = books.getFirst().getId();

        var bookOrderRequest =
                UserBookOrderRequest.builder()
                        .userId(userId)
                        .bookId(bookId)
                        .build();

        final UserBookOrderResponse bookOrderBorrow = userBookOrder(Path.BORROW_BOOK_PATH, bookOrderRequest);
        assertEquals(StatusType.BORROW, bookOrderBorrow.getStatusType());

        final UserBookOrderResponse bookOrderReturn = userBookOrder(Path.RETURN_BOOK_PATH, bookOrderRequest);
        assertEquals(StatusType.RETURN, bookOrderReturn.getStatusType());
    }

    @Test
    void test_Fail_Borrow_Book_Twice() throws Exception{

        var books = fetchList(Path.BOOK_PATH, BookResponse.class);
        var users = fetchList(Path.USER_PATH, UserResponse.class);

        var userId = users.getFirst().getId();
        var bookId = books.getFirst().getId();

        var bookOrderRequest =
                UserBookOrderRequest.builder()
                        .userId(userId)
                        .bookId(bookId)
                        .build();

        final UserBookOrderResponse bookOrderBorrow = userBookOrder(Path.BORROW_BOOK_PATH, bookOrderRequest);
        assertEquals(StatusType.BORROW, bookOrderBorrow.getStatusType());

        final ResponseEntity<ErrorResponse> exchange = restTemplate
                .exchange(Path.BORROW_BOOK_PATH,
                        POST,
                        new HttpEntity<>(bookOrderRequest),
                        ErrorResponse.class);

        assertEquals(exchange.getBody().getStatus(), HttpStatus.CONFLICT.getReasonPhrase());
        assertEquals(exchange.getBody().getStatusCode(), HttpStatus.CONFLICT.value());
    }

    @SneakyThrows
    @Test
    void test_Successful_Two_Users_Concurrent_Borrow_Book(){

        var books = fetchList(Path.BOOK_PATH, BookResponse.class);
        var users = fetchList(Path.USER_PATH, UserResponse.class);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        users.forEach(user -> {
            Runnable task = createTask( user.getId(), books.getFirst().getId(), latch);
            executor.submit(task);

        });
        latch.await();
        executor.shutdown();

        List<UsersBookOrder> listBorrowBook = userBookOrderRepository.findAll();

        Assertions.assertEquals(listBorrowBook.size(), 1);
    }

    private UserBookOrderResponse userBookOrder(String uri, UserBookOrderRequest bookOrderRequest){
        return  restTemplate.postForObject(uri, bookOrderRequest, UserBookOrderResponse.class);
    }

    private <T> List<T> fetchList(String url, Class<T> clazz) throws Exception {
        String json = restTemplate.getForObject(url, String.class);
        return Utils.convertToPageResponseContent(json, clazz);
    }

    private  Runnable createTask(Long userId, Long bookId,  CountDownLatch latch) {
        var bookOrderRequest = UserBookOrderRequest.builder().userId(userId).bookId(bookId).build();

        return () -> {
            try {
                userBookOrder(Path.BORROW_BOOK_PATH, bookOrderRequest); // each Runnable uses its own userId
            } finally {
                latch.countDown();
            }
        };
    }
}
