package com.education.flashEng.controller;

import com.education.flashEng.payload.request.LoginRequest;
import com.education.flashEng.payload.request.RegisterRequest;
import com.education.flashEng.payload.response.ApiResponse;
import com.education.flashEng.service.AuthenticateService;
import com.education.flashEng.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticateService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest) {
        ApiResponse<String> response = authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        ApiResponse<String> response = userService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
