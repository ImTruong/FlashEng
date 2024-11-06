package com.education.flashEng.service;

import com.education.flashEng.entity.ClassInvitationEntity;
import com.education.flashEng.entity.ClassJoinRequestEntity;
import com.education.flashEng.entity.NotificationEntity;
import com.education.flashEng.entity.NotificationMetaDataEntity;

public interface NotificationService {
    boolean createClassJoinRequestNotification(ClassJoinRequestEntity classJoinRequestEntity);

    boolean createClassInvitationNotification(ClassInvitationEntity classInvitationEntity);

    NotificationMetaDataEntity getNotificationMetaDataEntityByKeyAndValue(String key, String value);

    boolean deleteAllRelatedNotificationsByNotificationMetaData(String key, String value);

    boolean createAcceptedClassJoinRequestNotification(ClassJoinRequestEntity classJoinRequestEntity);

    boolean createRejectedClassJoinRequestNotification(ClassJoinRequestEntity classJoinRequestEntity);

    boolean createAcceptedClassInvitationNotification(ClassInvitationEntity classInvitationEntity);

    boolean createRejectedClassInvitationNotification(ClassInvitationEntity classInvitationEntity);
}
