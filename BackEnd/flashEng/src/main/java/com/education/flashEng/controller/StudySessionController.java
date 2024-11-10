package com.education.flashEng.controller;

import com.education.flashEng.payload.request.CreateClassRequest;
import com.education.flashEng.payload.request.StudySessionRequest;
import com.education.flashEng.payload.response.ApiResponse;
import com.education.flashEng.service.StudySessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/study")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    @PostMapping
    public ResponseEntity<?> createStudySession(@Valid @RequestBody StudySessionRequest studySessionRequest  ) {
        ApiResponse<String> response = new ApiResponse<>(studySessionService.createStudySession(studySessionRequest),"Class Created Successfully",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
