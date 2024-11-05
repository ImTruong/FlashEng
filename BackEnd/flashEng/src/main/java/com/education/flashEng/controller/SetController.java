package com.education.flashEng.controller;

import com.education.flashEng.enums.AccessModifierType;
import com.education.flashEng.payload.request.CreateSetRequest;
import com.education.flashEng.payload.response.ApiResponse;
import com.education.flashEng.service.SetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/set")
public class SetController {

    @Autowired
    SetService setService;

    @PostMapping
    public ResponseEntity<?> createSet(@Valid @RequestBody CreateSetRequest createSetRequest) {
        ApiResponse<?> response = new ApiResponse<>(setService.createSet(createSetRequest), "Create Set Successfully", AccessModifierType.type());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
