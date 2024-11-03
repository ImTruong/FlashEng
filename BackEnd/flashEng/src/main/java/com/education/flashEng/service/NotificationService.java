package com.education.flashEng.service;

import com.education.flashEng.entity.ClassInvitationEntity;
import com.education.flashEng.entity.ClassJoinRequestEntity;
import com.education.flashEng.entity.NotificationEntity;
import com.education.flashEng.entity.NotificationMetaDataEntity;

public interface NotificationService {
    boolean createClassJoinRequestNotification(ClassJoinRequestEntity classJoinRequestEntity);

    boolean createClassInvitationNotification(ClassInvitationEntity classInvitationEntity);

    NotificationMetaDataEntity getNotificationMetaDataEntityByKeyAndValue(String key, String value);

    boolean deleteNotificationEntityByEntity(NotificationEntity notificationEntity);

    boolean deleteNotificationMetaDataEntityByEntity(NotificationMetaDataEntity notificationMetaDataEntity);

    boolean deleteAllRelatedNotificationsByNotificationMetaData(String key, String value);

}
