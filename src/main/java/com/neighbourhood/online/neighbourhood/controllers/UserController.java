package com.neighbourhood.online.neighbourhood.controllers;

import com.neighbourhood.online.neighbourhood.models.User;
import com.neighbourhood.online.neighbourhood.payloads.RegistrationRequest;
import com.neighbourhood.online.neighbourhood.payloads.UserLoginRequest;
import com.neighbourhood.online.neighbourhood.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController{

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest userLoginRequest){
        return userService.loginUser(userLoginRequest);
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest){
        return userService.registerUser(registrationRequest);
    }

    @GetMapping("list_all")
    public ResponseEntity<?> getAllUsers(){
        return userService.getAllUsers();
    }
}
