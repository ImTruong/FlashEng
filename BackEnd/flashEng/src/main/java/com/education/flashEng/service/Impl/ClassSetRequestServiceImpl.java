package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.ClassSetRequestEntity;
import com.education.flashEng.entity.SetEntity;
import com.education.flashEng.entity.UserEntity;
import com.education.flashEng.repository.ClassSetRequestRepository;
import com.education.flashEng.service.ClassSetRequestService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassSetRequestServiceImpl implements ClassSetRequestService {

    @Autowired
    private ClassSetRequestRepository classSetRequestRepository;

    @Transactional
    @Override
    public ClassSetRequestEntity createSetRequest(SetEntity setEntity, UserEntity userEntity) {
        ClassSetRequestEntity classSetRequestEntity = new ClassSetRequestEntity();
        classSetRequestEntity.setTitle(setEntity.getName());
        classSetRequestEntity.setClassEntity(setEntity.getClassEntity());
        classSetRequestEntity.setUserEntity(userEntity);
        classSetRequestEntity.setSetEntity(setEntity);
        classSetRequestRepository.save(classSetRequestEntity);
        return classSetRequestEntity;
    }
}
