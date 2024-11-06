package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.*;
import com.education.flashEng.enums.AccessModifierType;
import com.education.flashEng.exception.EntityNotFoundWithIdException;
import com.education.flashEng.payload.DTO.SetDTO;
import com.education.flashEng.payload.request.CreateSetRequest;
import com.education.flashEng.repository.ClassMemberRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    @Autowired
    private ClassMemberRepository classMemberRepository;

    @Transactional
    @Override
    public boolean createSet(CreateSetRequest createSetRequest) {
        UserEntity user = userService.getUserFromSecurityContext();

        SetEntity setEntity = modelMapper.map(createSetRequest, SetEntity.class);
        setEntity.setUserEntity(user);

        //Kiểm tra privacy của set
        if(setEntity.getPrivacyStatus().equals(AccessModifierType.getKeyfromValue("Class"))){
            ClassEntity classEntity = classRepository.findById(createSetRequest.getClassId())
                    .orElseThrow(() -> new EntityNotFoundWithIdException("ClassEntity", createSetRequest.getClassId().toString()));
            ClassMemberEntity classMemberEntity = classMemberRepository.findById(user.getId())
                    .orElseThrow(() -> new EntityNotFoundWithIdException("ClassMemberEntity", user.getId().toString()));

            if(classMemberEntity.getRoleClassEntity().getName().equals("ADMIN")){
                setEntity.setClassEntity(classEntity);
                setRepository.save(setEntity);
            }
            else{
                setEntity.setPrivacyStatus(String.valueOf(AccessModifierType.getKeyfromValue("Private")));
                setRepository.save(setEntity);
                ClassSetRequestEntity classSetRequestEntity = classSetRequestService.createSetRequest(setEntity, user, classEntity);
                notificationService.createClassSetRequestNotification(classSetRequestEntity);
            }
        }
        else{
            setRepository.save(setEntity);
        }

        return true;
    }

    @Override
    public List<SetEntity> getPublicSet() {
        List<SetEntity> setEntities = setRepository.findAll();
        List<SetDTO> setDTOS = new ArrayList<>();
        for(SetEntity setEntity : setEntities){
            SetDTO s = new SetDTO();
            modelMapper.map(setEntity, s);
            setDTOS.add(s);
        }
        return setEntities;
    }
}
