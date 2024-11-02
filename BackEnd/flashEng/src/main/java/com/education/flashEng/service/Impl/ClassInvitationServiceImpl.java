package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.*;
import com.education.flashEng.exception.EntityNotFoundWithIdException;
import com.education.flashEng.repository.ClassInvitationRepository;
import com.education.flashEng.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClassInvitationServiceImpl implements ClassInvitationService {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

    @Autowired
    private RoleClassService roleClassService;

    @Autowired
    private ClassInvitationRepository classInvitationRepository;

    @Autowired
    private ClassMemberService classMemberService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ClassJoinRequestService classJoinRequestService;

    @Transactional
    @Override
    public boolean inviteToClass(Long classId, Long inviteeId) {
        UserEntity invitee = userService.getUserById(inviteeId);
        UserEntity inviter = userService.getUserFromSecurityContext();
        ClassEntity classEntity = classService.getClassById(classId);
        if (classEntity.getClassMemberEntityList().stream().noneMatch(classMemberEntity -> classMemberEntity.getUserEntity() == inviter))
            throw new AccessDeniedException("You are not authorized to invite to this class.");
        if (classEntity.getClassMemberEntityList().stream().anyMatch(classMemberEntity -> classMemberEntity.getUserEntity() == invitee))
            throw new AccessDeniedException("User is already a member of this class.");
        if (classInvitationRepository.findByClassEntityIdAndInviteeEntityIdAndInviterEntityId(classId, inviteeId, inviter.getId()).isPresent())
            throw new AccessDeniedException("You have already invited this user to this class.");
        ClassInvitationEntity classInvitationEntity = ClassInvitationEntity.builder()
                .classEntity(classEntity)
                .inviteeEntity(invitee)
                .inviterEntity(inviter)
                .build();
        classInvitationRepository.save(classInvitationEntity);
        notificationService.createClassInvitationNotification(classInvitationEntity);
        return true;
    }

    @Transactional
    @Override
    public boolean acceptInvitation(Long invitationId) {
        ClassInvitationEntity classInvitationEntity = classInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new EntityNotFoundWithIdException("Invitation", invitationId.toString()));
        UserEntity invitee = userService.getUserFromSecurityContext();
        if (invitee != classInvitationEntity.getInviteeEntity())
            throw new AccessDeniedException("You are not authorized to accept this invitation.");
        ClassMemberEntity classMemberEntity = classMemberService.getClassMemberByClassIdAndUserId(classInvitationEntity.getClassEntity().getId(), classInvitationEntity.getInviterEntity().getId());
        classInvitationRepository.delete(classInvitationEntity);
        if (classMemberEntity.getRoleClassEntity().getName().equals("ADMIN")){
            classMemberService.saveClassMember(ClassMemberEntity.builder()
                    .classEntity(classInvitationEntity.getClassEntity())
                    .userEntity(invitee)
                    .roleClassEntity(roleClassService.getRoleClassByName("MEMBER"))
                    .build());
        }
        else
            classJoinRequestService.createClassJoinRequest(classInvitationEntity.getClassEntity().getId(), invitee.getId());
        notificationService.deleteAllRelatedNotificationsByNotificationMetaData("classInvitationId", invitationId.toString());
        return true;
    }

    @Transactional
    @Override
    public boolean rejectInvitation(Long invitationId) {
        ClassInvitationEntity classInvitationEntity = classInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new EntityNotFoundWithIdException("Invitation", invitationId.toString()));
        UserEntity invitee = userService.getUserFromSecurityContext();
        if (invitee != classInvitationEntity.getInviteeEntity())
            throw new AccessDeniedException("You are not authorized to reject this invitation.");
        classInvitationRepository.delete(classInvitationEntity);
        notificationService.deleteAllRelatedNotificationsByNotificationMetaData("classInvitationId", invitationId.toString());
        return true;
    }


}
