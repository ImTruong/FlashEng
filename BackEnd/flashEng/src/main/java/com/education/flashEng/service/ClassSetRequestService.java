package com.education.flashEng.service;

import com.education.flashEng.entity.ClassEntity;
import com.education.flashEng.entity.ClassSetRequestEntity;
import com.education.flashEng.entity.SetEntity;
import com.education.flashEng.entity.UserEntity;

public interface ClassSetRequestService {
    public ClassSetRequestEntity createSetRequest(SetEntity setEntity, UserEntity userEntity, ClassEntity classEntity);
    public boolean acceptSetRequest(Long SetRequestId);
}
