package com.education.flashEng.controller;

import com.education.flashEng.entity.ClassInvitationEntity;
import com.education.flashEng.payload.request.ClassNameChangeRequest;
import com.education.flashEng.payload.request.CreateClassRequest;
import com.education.flashEng.payload.request.LoginRequest;
import com.education.flashEng.payload.response.ApiResponse;
import com.education.flashEng.service.ClassInvitationService;
import com.education.flashEng.service.ClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/class")

public class ClassController {

    @Autowired
    private ClassService classService;


    @PostMapping
    public ResponseEntity<?> createClass(@Valid @RequestBody CreateClassRequest createClassRequest) {
        ApiResponse<String> response = new ApiResponse<>(classService.createClass(createClassRequest),"Class Created Successfully",null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/name")
    public ResponseEntity<?> updateClassName(@Valid @RequestBody ClassNameChangeRequest classNameChangeRequest) {
        ApiResponse<String> response = new ApiResponse<>(classService.updateClassName(classNameChangeRequest.getClassId(), classNameChangeRequest.getName()), "Class Name Updated Successfully", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
