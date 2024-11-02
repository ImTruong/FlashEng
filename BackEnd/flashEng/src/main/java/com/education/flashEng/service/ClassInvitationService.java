package com.education.flashEng.service;

import com.education.flashEng.entity.ClassInvitationEntity;

import java.util.Optional;

public interface ClassInvitationService {

     boolean inviteToClass(Long classId, Long inviteeID);

     boolean acceptInvitation(Long invitationId);

     boolean rejectInvitation(Long invitationId);

}
