package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.*;
import com.education.flashEng.enums.AccessModifierType;
import com.education.flashEng.payload.request.CreateSetRequest;
import com.education.flashEng.repository.ClassRepository;
import com.education.flashEng.repository.ClassSetRequestRepository;
import com.education.flashEng.repository.SetRepository;
import com.education.flashEng.service.ClassSetRequestService;
import com.education.flashEng.service.NotificationService;
import com.education.flashEng.service.SetService;
import com.education.flashEng.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SetServiceImpl implements SetService {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SetRepository setRepository;
    @Autowired
    private ClassSetRequestService classSetRequestService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ClassRepository classRepository;

    @Transactional
    @Override
    public boolean createSet(CreateSetRequest createSetRequest) {
        UserEntity user = userService.getUserFromSecurityContext();

        SetEntity setEntity = modelMapper.map(createSetRequest, SetEntity.class);
        setEntity.setUserEntity(user);

        System.out.println(AccessModifierType.getKeyfromValue("Class"));
        if(setEntity.getPrivacyStatus().equals(AccessModifierType.getKeyfromValue("Class"))){
            Optional<ClassEntity> classEntity = classRepository.findById(createSetRequest.getClassId());
            setEntity.setClassEntity(classEntity.get());
            setEntity.setPrivacyStatus(String.valueOf(AccessModifierType.valueOf("PRIVATE")));
            setRepository.save(setEntity);
            ClassSetRequestEntity classSetRequestEntity = classSetRequestService.createSetRequest(setEntity, user);
            notificationService.createClassSetRequestNotification(classSetRequestEntity);

        }
        else{
            setRepository.save(setEntity);
        }

        return true;
    }
}
