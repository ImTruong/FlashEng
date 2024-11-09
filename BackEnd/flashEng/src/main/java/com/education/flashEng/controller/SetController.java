package com.education.flashEng.controller;

import com.education.flashEng.payload.request.CreateSetRequest;
import com.education.flashEng.payload.request.UpdateSetRequest;
import com.education.flashEng.payload.response.ApiResponse;
import com.education.flashEng.service.SetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/private")
    public ResponseEntity<?> getPrivateSets() {
        ApiResponse<?> response = new ApiResponse<>(true, "Get All Private Sets Of Current User Successfully", setService.getPrivateSet());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getSetsOfClass(@PathVariable Long classId) {
        ApiResponse<?> response = new ApiResponse<>(true, "Get All Private Sets Of Current User Successfully", setService.getSetByClassID(classId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createSet(@Valid @RequestBody CreateSetRequest createSetRequest) {
        ApiResponse<?> response = new ApiResponse<>(setService.createSet(createSetRequest), "Create Set Successfully", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateSet(@Valid @RequestBody UpdateSetRequest setUpdateRequest) {
        ApiResponse<?> response = new ApiResponse<>(setService.updateSet(setUpdateRequest), "Update Set Successfully", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{setId}")
    public ResponseEntity<?> deleteSet(@PathVariable Long setId) {
        ApiResponse<?> response = new ApiResponse<>(setService.deleteSetById(setId), "Delete Set Successfully", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}