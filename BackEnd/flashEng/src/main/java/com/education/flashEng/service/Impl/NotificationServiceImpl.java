package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.*;
import com.education.flashEng.exception.EntityNotFoundWithIdException;
import com.education.flashEng.payload.response.NotificationResponse;
import com.education.flashEng.repository.NotificationMetaDataRepository;
import com.education.flashEng.repository.NotificationRepository;
import com.education.flashEng.service.ClassInvitationService;
import com.education.flashEng.service.NotificationService;
import com.education.flashEng.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMetaDataRepository notificationMetaDataRepository;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public boolean createClassJoinRequestNotification(ClassJoinRequestEntity classJoinRequestEntity) {
        NotificationMetaDataEntity notificationMetaDataEntity = NotificationMetaDataEntity.builder()
                .key("classJoinRequestId")
                .value(classJoinRequestEntity.getId().toString())
                .build();
        notificationMetaDataRepository.save(notificationMetaDataEntity);
        ClassEntity classEntity = classJoinRequestEntity.getClassEntity();
        for (ClassMemberEntity memberEntity : classEntity.getClassMemberEntityList()){
            if(memberEntity.getRoleClassEntity().getName().equals("ADMIN")){
                UserEntity user = memberEntity.getUserEntity();
                NotificationEntity notificationEntity = NotificationEntity.builder()
                        .userEntity(user)
                        .message("User " + classJoinRequestEntity.getUserEntity().getUsername() + " has requested to join class " + classEntity.getName())
                        .notificationMetaDataEntity(notificationMetaDataEntity)
                        .isRead(false)
                        .type("CLASS_JOIN_REQUEST")
                        .build();
                notificationRepository.save(notificationEntity);
            }
        }
        return true;
    }

    @Transactional
    @Override
    public boolean createClassInvitationNotification(ClassInvitationEntity classInvitationEntity) {
        NotificationMetaDataEntity notificationMetaDataEntity = NotificationMetaDataEntity.builder()
                .key("classInvitationId")
                .value(classInvitationEntity.getId().toString())
                .build();
        notificationMetaDataRepository.save(notificationMetaDataEntity);
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .userEntity(classInvitationEntity.getInviteeEntity())
                .message(classInvitationEntity.getInviterEntity().getUsername() + " have invited you to join class " + classInvitationEntity.getClassEntity().getName())
                .notificationMetaDataEntity(notificationMetaDataEntity)
                .isRead(false)
                .type("CLASS_INVITATION")
                .build();
        notificationRepository.save(notificationEntity);
        return true;
    }

    @Override
    public NotificationMetaDataEntity getNotificationMetaDataEntityByKeyAndValue(String key, String value) {
        return notificationMetaDataRepository.findByKeyAndValue(key, value)
                .orElseThrow(() -> new EntityNotFoundWithIdException("NotificationMetaData", "key: " + key + " value: " + value));
    }

    @Transactional
    @Override
    public boolean deleteAllRelatedNotificationsByNotificationMetaData(String key, String value) {
        NotificationMetaDataEntity notificationMetaDataEntity = notificationMetaDataRepository.findByKeyAndValue(key, value)
                .orElseThrow(() -> new EntityNotFoundWithIdException("NotificationMetaData", "key: " + key + " value: " + value));
        notificationRepository.deleteAll(notificationMetaDataEntity.getNotificationEntityList());
        notificationMetaDataRepository.delete(notificationMetaDataEntity);
        return true;
    }

    @Override
    public boolean createAcceptedClassJoinRequestNotification(ClassJoinRequestEntity classJoinRequestEntity) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .userEntity(classJoinRequestEntity.getUserEntity())
                .message("Your request to join class " + classJoinRequestEntity.getClassEntity().getName() + " has been accepted.")
                .isRead(false)
                .type("ACCEPTED_CLASS_JOIN_REQUEST")
                .build();
        notificationRepository.save(notificationEntity);
        return true;
    }

    @Override
    public boolean createRejectedClassJoinRequestNotification(ClassJoinRequestEntity classJoinRequestEntity) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .userEntity(classJoinRequestEntity.getUserEntity())
                .message("Your request to join class " + classJoinRequestEntity.getClassEntity().getName() + " has been rejected.")
                .isRead(false)
                .type("REJECTED_CLASS_JOIN_REQUEST")
                .build();
        notificationRepository.save(notificationEntity);
        return true;
    }

    @Override
    public boolean createAcceptedClassInvitationNotification(ClassInvitationEntity classInvitationEntity) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .userEntity(classInvitationEntity.getInviterEntity())
                .message(classInvitationEntity.getInviteeEntity().getUsername() + " has accepted your invitation to join class " + classInvitationEntity.getClassEntity().getName())
                .isRead(false)
                .type("ACCEPTED_CLASS_INVITATION")
                .build();
        notificationRepository.save(notificationEntity);
        return true;
    }

    @Override
    public boolean createRejectedClassInvitationNotification(ClassInvitationEntity classInvitationEntity) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .userEntity(classInvitationEntity.getInviterEntity())
                .message(classInvitationEntity.getInviteeEntity().getUsername() + " has rejected your invitation to join class " + classInvitationEntity.getClassEntity().getName())
                .isRead(false)
                .type("REJECTED_CLASS_INVITATION")
                .build();
        notificationRepository.save(notificationEntity);
        return true;
    }

    @Transactional
    @Override
    public boolean createClassSetRequestNotification(ClassSetRequestEntity classSetRequestEntity) {
        NotificationMetaDataEntity notificationMetaDataEntity = NotificationMetaDataEntity.builder()
                .key("classSetRequestId")
                .value(classSetRequestEntity.getId().toString())
                .build();
        notificationMetaDataRepository.save(notificationMetaDataEntity);
        ClassEntity classEntity = classSetRequestEntity.getClassEntity();
        for (ClassMemberEntity memberEntity : classEntity.getClassMemberEntityList()) {
            if (memberEntity.getRoleClassEntity().getName().equals("ADMIN")) {
                UserEntity user = memberEntity.getUserEntity();
                NotificationEntity notificationEntity = NotificationEntity.builder()
                        .userEntity(user)
                        .message("User " + classSetRequestEntity.getUserEntity().getUsername() + " has requested to create set in class " + classEntity.getName())
                        .notificationMetaDataEntity(notificationMetaDataEntity)
                        .isRead(false)
                        .type("CLASS_SET_REQUEST")
                        .build();
                notificationRepository.save(notificationEntity);
            }
        }
        return true;
    }

    @Transactional
    @Override
    public boolean createAcceptRequestNotification(SetEntity setEntity) {
        NotificationMetaDataEntity notificationMetaDataEntity = NotificationMetaDataEntity.builder()
                .key("setId")
                .value(setEntity.getId().toString())
                .build();
        notificationMetaDataRepository.save(notificationMetaDataEntity);
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .userEntity(setEntity.getUserEntity())
                .message("Your set request in class " + setEntity.getClassEntity().getName() + " has been accepted.")
                .notificationMetaDataEntity(notificationMetaDataEntity)
                .isRead(false)
                .type("CLASS_SET_ACCEPT")
                .build();
        notificationRepository.save(notificationEntity);
        return true;
    }

    @Override
    public boolean createRejectRequestNotification(SetEntity setEntity, ClassEntity classEntity) {
        NotificationMetaDataEntity notificationMetaDataEntity = NotificationMetaDataEntity.builder()
                .key("setId")
                .value(setEntity.getId().toString())
                .build();
        notificationMetaDataRepository.save(notificationMetaDataEntity);
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .userEntity(setEntity.getUserEntity())
                .message("Your set request in class " + classEntity.getName() + " has been rejected.")
                .notificationMetaDataEntity(notificationMetaDataEntity)
                .isRead(false)
                .type("CLASS_SET_REJECT")
                .build();
        notificationRepository.save(notificationEntity);
        return true;
    }

    @Override
    public boolean createStudySessionNotification(StudySessionEntity studySessionEntity, LocalDateTime reminderTime) {
        NotificationMetaDataEntity notificationMetaDataEntity = NotificationMetaDataEntity.builder()
                .key("WordId")
                .value(studySessionEntity.getWordEntity().getId().toString())
                .build();
        notificationMetaDataRepository.save(notificationMetaDataEntity);
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .userEntity(studySessionEntity.getUserEntity())
                .message("Now is the moment to pick up '" + studySessionEntity.getWordEntity().getWord() + "' word")
                .notificationMetaDataEntity(notificationMetaDataEntity)
                .isRead(false)
                .type("REMINDER_STUDY_SESSION")
                .reminderTime(reminderTime)
                .build();
        notificationRepository.save(notificationEntity);
        return true;
    }

    @Override
    public List<NotificationResponse> getAllCurrentUserNotifications() {
        UserEntity user = userService.getUserFromSecurityContext();
        List<NotificationEntity> notificationEntityList = user.getNotificationsEntityList();

        return notificationEntityList.stream()
                .filter(notificationEntity -> notificationEntity.getReminderTime() == null || !notificationEntity.getReminderTime().isBefore(LocalDateTime.now()))
                .map(notificationEntity -> {
                    notificationEntity.setIsRead(true);

                    NotificationResponse.NotificationResponseBuilder responseBuilder = NotificationResponse.builder()
                            .message(notificationEntity.getMessage())
                            .type(notificationEntity.getType())
                            .isRead(true);

                    if (notificationEntity.getNotificationMetaDataEntity() != null) {
                        Map<String, String> additionalInfo = Map.of(
                                notificationEntity.getNotificationMetaDataEntity().getKey(),
                                notificationEntity.getNotificationMetaDataEntity().getValue()
                        );
                        responseBuilder.additionalInfo(additionalInfo);
                    }

                    return responseBuilder.build();
                })
                .toList();
    }

}
