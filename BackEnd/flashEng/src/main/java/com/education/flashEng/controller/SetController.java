package com.education.flashEng.controller;

import com.education.flashEng.entity.SetEntity;
import com.education.flashEng.enums.AccessModifierType;
import com.education.flashEng.payload.DTO.SetDTO;
import com.education.flashEng.payload.request.CreateSetRequest;
import com.education.flashEng.payload.response.ApiResponse;
import com.education.flashEng.service.SetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/set")
public class SetController {

    @Autowired
    SetService setService;

    @GetMapping("/public")
    public ResponseEntity<?> getPublicSets() {
        ApiResponse<?> response = new ApiResponse<>(true, "Get All Public Sets Successfully", setService.getPublicSet());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createSet(@Valid @RequestBody CreateSetRequest createSetRequest) {
        ApiResponse<?> response = new ApiResponse<>(setService.createSet(createSetRequest), "Create Set Successfully", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
