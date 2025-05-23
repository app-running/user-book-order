package com.book.order.contorller;


import com.book.order.dto.UserRequest;
import com.book.order.dto.UserResponse;
import com.book.order.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UsersService usersService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "100", name = "perPage") int perPage) {
        return ResponseEntity.ok(usersService.getAllUser(page, perPage));
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createNewUser(@RequestBody final UserRequest userRequest) {
        return ResponseEntity.ok(usersService.addUser(userRequest));
    }

}
