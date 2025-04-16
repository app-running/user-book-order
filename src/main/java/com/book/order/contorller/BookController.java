package com.book.order.contorller;

import com.book.order.service.BookService;
import com.book.order.dto.BookRequest;
import com.book.order.dto.BookResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks( @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "100", name = "perPage") int perPage){
        return  ResponseEntity.ok(bookService.getBooks(page, perPage).getContent());
    }

    @PostMapping("/addBook")
    public ResponseEntity<BookResponse> addBook(@RequestBody @Valid  BookRequest bookRequest){
       return  ResponseEntity.ok(bookService.addBook(bookRequest));
    }
}
