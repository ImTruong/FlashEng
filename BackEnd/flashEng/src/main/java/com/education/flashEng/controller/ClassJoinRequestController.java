package com.education.flashEng.controller;

import com.education.flashEng.payload.request.ClassJoinRequestRequest;
import com.education.flashEng.payload.response.ApiResponse;
import com.education.flashEng.service.ClassJoinRequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/class/request/join")
public class ClassJoinRequestController {

    @Autowired
    private ClassJoinRequestService classJoinRequestService;

    @PostMapping()
    public ResponseEntity<?> createClassJoinRequest(@Valid @RequestBody ClassJoinRequestRequest classJoinRequestRequest) {
        try {
            classJoinRequestService.createClassJoinRequest(classJoinRequestRequest.getClassId(), classJoinRequestRequest.getUserId());
            ApiResponse<String> response = new ApiResponse<>(true, "Join Request Created Successfully.", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(false, "Failed to create join request: " + e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptClassJoinRequest(@RequestParam @NotNull(message = "joinRequestId is required") Long requestId) {
        ApiResponse<String> response = new ApiResponse<>(classJoinRequestService.acceptClassJoinRequest(requestId), "Join Request Accepted Successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/reject")
    public ResponseEntity<?> rejectClassJoinRequest(@RequestParam @NotNull(message = "joinRequestId is required") Long requestId) {
        ApiResponse<String> response = new ApiResponse<>(classJoinRequestService.rejectClassJoinRequest(requestId), "Request Rejected Successfully", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}