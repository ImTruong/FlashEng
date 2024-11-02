package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.*;
import com.education.flashEng.exception.EntityNotFoundWithIdException;
import com.education.flashEng.repository.ClassJoinRequestRepository;
import com.education.flashEng.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassJoinRequestServiceImpl implements ClassJoinRequestService {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

    @Autowired
    private ClassJoinRequestRepository classJoinRequestRepository;

    @Autowired
    private ClassMemberService classMemberService;

    @Autowired
    private RoleClassService roleClassService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Optional<ClassJoinRequestEntity> getClassJoinRequestByClassIdAndUserId(Long classId, Long userId) {
        return classJoinRequestRepository.findByClassEntityIdAndUserEntityId(classId, userId);
    }

    @Transactional
    @Override
    public ClassJoinRequestEntity createClassJoinRequest(Long classId, Long userId) {
        UserEntity user = userService.getUserById(userId);
        if(user != userService.getUserFromSecurityContext())
            throw new AccessDeniedException("You are not authorized to create this request.");
        if(classJoinRequestRepository.findByClassEntityIdAndUserEntityId(classId, userId).isPresent())
            throw new AccessDeniedException("You have already requested to join this class.");
        ClassEntity classEntity = classService.getClassById(classId);
        if (classEntity.getClassMemberEntityList().stream().anyMatch(classMemberEntity -> classMemberEntity.getUserEntity() == user))
            throw new AccessDeniedException("You are already a member of this class.");

        ClassJoinRequestEntity classJoinRequestEntity = ClassJoinRequestEntity.builder()
                .classEntity(classEntity)
                .userEntity(user)
                .build();
        classJoinRequestRepository.save(classJoinRequestEntity);
        notificationService.createClassJoinRequestNotification(classJoinRequestEntity);
        return classJoinRequestEntity;
    }

    @Transactional
    @Override
    public boolean acceptClassJoinRequest(Long requestId) {
        ClassJoinRequestEntity classJoinRequestEntity = classJoinRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundWithIdException("Class Join Request", requestId.toString()));
        UserEntity user = userService.getUserFromSecurityContext();
        ClassEntity classEntity = classJoinRequestEntity.getClassEntity();
        List<ClassMemberEntity> classMemberEntityList = classEntity.getClassMemberEntityList();
        for (ClassMemberEntity classMemberEntity : classMemberEntityList) {
            if (classMemberEntity.getUserEntity().equals(user)) {
                if(classMemberEntity.getRoleClassEntity().getName().equals("ADMIN")){
                    classMemberService.saveClassMember(ClassMemberEntity.builder()
                            .classEntity(classJoinRequestEntity.getClassEntity())
                            .userEntity(classJoinRequestEntity.getUserEntity())
                            .roleClassEntity(roleClassService.getRoleClassByName("MEMBER"))
                            .build());
                    notificationService.deleteAllRelatedNotificationsByNotificationMetaData("classJoinRequestId", requestId.toString());
                    classJoinRequestRepository.delete(classJoinRequestEntity);
                    return true;
                }
                else
                    throw new AccessDeniedException("You are not authorized to accept this request.");
            }
        }
        throw new AccessDeniedException("You are not authorized to accept this request.");
    }

    @Transactional
    @Override
    public boolean rejectClassJoinRequest(Long requestId) {
        ClassJoinRequestEntity classJoinRequestEntity = classJoinRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundWithIdException("Class Join Request", requestId.toString()));
        UserEntity user = userService.getUserFromSecurityContext();
        ClassEntity classEntity = classJoinRequestEntity.getClassEntity();
        List<ClassMemberEntity> classMemberEntityList = classEntity.getClassMemberEntityList();
        for (ClassMemberEntity classMemberEntity : classMemberEntityList) {
            if (classMemberEntity.getUserEntity().equals(user)) {
                if(classMemberEntity.getRoleClassEntity().getName().equals("ADMIN")){
                    classJoinRequestRepository.delete(classJoinRequestEntity);
                    notificationService.deleteAllRelatedNotificationsByNotificationMetaData("classJoinRequestId", requestId.toString());
                    classJoinRequestRepository.delete(classJoinRequestEntity);
                    return true;
                }
                else
                    throw new AccessDeniedException("You are not authorized to reject this request.");
            }
        }
        throw new AccessDeniedException("You are not authorized to reject this request.");
    }
}