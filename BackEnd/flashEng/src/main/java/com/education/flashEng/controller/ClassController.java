package com.education.flashEng.controller;

import com.education.flashEng.payload.request.CreateClassRequest;
import com.education.flashEng.payload.request.LoginRequest;
import com.education.flashEng.payload.response.ApiResponse;
import com.education.flashEng.service.ClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/class")

public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping
    public ResponseEntity<?> createClass(@Valid @RequestBody CreateClassRequest createClassRequest) {
        ApiResponse<String> response = new ApiResponse<String>(classService.createClass(createClassRequest),"Class Created Successfully",null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
