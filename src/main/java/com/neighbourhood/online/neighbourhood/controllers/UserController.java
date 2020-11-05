package com.neighbourhood.online.neighbourhood.controllers;

import com.neighbourhood.online.neighbourhood.payloads.RegistrationRequest;
import com.neighbourhood.online.neighbourhood.payloads.UserLoginRequest;
import com.neighbourhood.online.neighbourhood.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("find")
    public ResponseEntity<?> findUser(@RequestParam String userId){
        return userService.findUser(userId);
    }

    @PostMapping("update_profile_picture")
    public ResponseEntity<?> updateProfilePicture(@RequestParam("file") MultipartFile profilePicture
            ,@RequestParam("user_id") String userId){
        return userService.updateProfilePicture(profilePicture,userId);
    }

    @GetMapping("get_profile_picture")
    public ResponseEntity<?> getProfilePicture(@RequestParam String userId){
        return userService.getProfilePicture(userId);
    }
}
