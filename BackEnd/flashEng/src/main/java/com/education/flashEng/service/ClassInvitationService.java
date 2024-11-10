package com.education.flashEng.service;

import com.education.flashEng.entity.ClassInvitationEntity;
import com.education.flashEng.payload.response.ClassInvitationResponse;

import java.util.List;
import java.util.Optional;

public interface ClassInvitationService {

     boolean inviteToClass(Long classId, String inviteeUsername);

     boolean acceptInvitation(Long invitationId);

     boolean rejectInvitation(Long invitationId);

     boolean deleteAllInviteeInvitationsOfAClass(Long classId, Long inviteeId);

     ClassInvitationResponse getClassInvitation(Long invitationId);
}

