package com.education.flashEng.controller;

import com.education.flashEng.payload.response.ApiResponse;
import com.education.flashEng.service.ClassInvitationService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/class/invitation")
public class ClassInvitationController {

    @Autowired
    private ClassInvitationService classInvitationService;

    @GetMapping("/{invitationId}")
    public ResponseEntity<?> getClassInvitation(@PathVariable @NotNull(message = "invitationId is required") Long invitationId) {
        ApiResponse<?> response = new ApiResponse<>(true, "Class Invitation Fetched Successfully", classInvitationService.getClassInvitation(invitationId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> inviteToClass(@RequestParam @NotNull(message = "classId is required") Long classId,
                                           @RequestParam @NotBlank(message = "inviteeUsername cannot be blank") String inviteeUsername) {
        ApiResponse<String> response = new ApiResponse<>(classInvitationService.inviteToClass(classId, inviteeUsername),"Invitation Sent Successfully",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptInvitation(@RequestParam @NotNull(message = "invitationId is required") Long invitationId) {
        ApiResponse<String> response = new ApiResponse<>(classInvitationService.acceptInvitation(invitationId),"Invitation Accepted Successfully",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/reject")
    public ResponseEntity<?> rejectInvitation(@RequestParam @NotNull(message = "invitationId is required") Long invitationId) {
        ApiResponse<String> response = new ApiResponse<>(classInvitationService.rejectInvitation(invitationId),"Invitation Rejected Successfully",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
