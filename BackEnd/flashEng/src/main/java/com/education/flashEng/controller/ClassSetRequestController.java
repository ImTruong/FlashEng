package com.education.flashEng.controller;

import com.education.flashEng.payload.response.ApiResponse;
import com.education.flashEng.service.ClassSetRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/set/request")
public class ClassSetRequestController {
    @Autowired
    ClassSetRequestService classSetRequestService;

    @PutMapping("/accept/{setRequestId}")
    public ResponseEntity<?> acceptSetRequest(Long setRequestId) {
        ApiResponse<?> response = new ApiResponse<>(classSetRequestService.acceptSetRequest(setRequestId), "Accept Set Request Successfully", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
