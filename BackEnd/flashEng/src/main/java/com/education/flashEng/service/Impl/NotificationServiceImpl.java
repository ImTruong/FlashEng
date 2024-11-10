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
