package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.*;
import com.education.flashEng.exception.EntityNotFoundWithIdException;
import com.education.flashEng.repository.NotificationMetaDataRepository;
import com.education.flashEng.repository.NotificationRepository;
import com.education.flashEng.service.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMetaDataRepository notificationMetaDataRepository;


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
                        .message("User " + user.getUsername() + " has requested to join class " + classEntity.getName())
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
                .message("You have been invited to join class " + classInvitationEntity.getClassEntity().getName())
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
    public boolean deleteNotificationEntityByEntity(NotificationEntity notificationEntity) {
        notificationRepository.delete(notificationEntity);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteNotificationMetaDataEntityByEntity(NotificationMetaDataEntity notificationMetaDataEntity) {
        notificationMetaDataRepository.delete(notificationMetaDataEntity);
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
}
