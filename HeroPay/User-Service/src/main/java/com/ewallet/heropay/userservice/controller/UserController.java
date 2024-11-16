package com.ewallet.heropay.userservice.controller;


import com.ewallet.heropay.userservice.dto.CreateUserRequest;
import com.ewallet.heropay.userservice.dto.UserResponse;
import com.ewallet.heropay.userservice.model.User;
import com.ewallet.heropay.userservice.service.UserService;
import com.ewallet.heropay.userservice.utility.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public void createUser(@RequestBody @Valid CreateUserRequest createUserRequest) throws JsonProcessingException {
        userService.create(Utils.convertUserCreateRequest(createUserRequest));
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable("userId") int userId) throws Exception {
        User user= userService.get(userId);
        return Utils.convertToUserResponse(user);
    }

    @GetMapping("/phone/{phone}")
    public UserResponse getUserByPhone(@PathVariable("phone") String phone) throws Exception {
        User user= userService.getByPhone(phone);
        return Utils.convertToUserResponse(user);
    }
}
