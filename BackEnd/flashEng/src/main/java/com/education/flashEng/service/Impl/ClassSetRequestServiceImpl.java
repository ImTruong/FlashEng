package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.ClassEntity;
import com.education.flashEng.entity.ClassSetRequestEntity;
import com.education.flashEng.entity.SetEntity;
import com.education.flashEng.entity.UserEntity;
import com.education.flashEng.enums.AccessModifierType;
import com.education.flashEng.exception.EntityNotFoundWithIdException;
import com.education.flashEng.exception.ResourceAlreadyExistsException;
import com.education.flashEng.repository.ClassMemberRepository;
import com.education.flashEng.repository.ClassSetRequestRepository;
import com.education.flashEng.repository.SetRepository;
import com.education.flashEng.service.ClassSetRequestService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ClassSetRequestServiceImpl implements ClassSetRequestService {

    @Autowired
    private ClassSetRequestRepository classSetRequestRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SetRepository setRepository;
    @Autowired
    private ClassMemberRepository classMemberRepository;

    @Transactional
    @Override
    public ClassSetRequestEntity createSetRequest(SetEntity setEntity, UserEntity userEntity, ClassEntity classEntity) {
        ClassSetRequestEntity classSetRequestEntity = new ClassSetRequestEntity();
        classSetRequestEntity.setTitle(setEntity.getName());
        classSetRequestEntity.setClassEntity(classEntity);
        classSetRequestEntity.setUserEntity(userEntity);
        classSetRequestEntity.setSetEntity(setEntity);
        classSetRequestRepository.save(classSetRequestEntity);
        return classSetRequestEntity;
    }

    @Transactional
    @Override
    public boolean acceptSetRequest(Long SetRequestId) {
//        UserEntity userEntity = userService.getUserFromSecurityContext();
//        ClassSetRequestEntity classSetRequestEntity = classSetRequestRepository.findById(SetRequestId)
//                .orElseThrow(() -> new EntityNotFoundWithIdException("Set Request", SetRequestId.toString()));
//        ClassEntity classEntity = classSetRequestEntity.getClassEntity();
//        List<UserEntity> adminOfClass = classMemberRepository.
//        if(classSetRequestEntity.get){
//            throw new AccessDeniedException("You do not permission to accept this request.");
//        }
//        SetEntity setEntity = classSetRequestEntity.getSetEntity();
//        if(         setEntity.getPrivacyStatus().equals(AccessModifierType.getKeyfromValue("Class"))
//                &&  setEntity.getClassEntity().equals(classEntity)){
//            throw new ResourceAlreadyExistsException("Set is already in this class.");
//        }else{
//            setEntity.setClassEntity(classEntity);
//            setEntity.setPrivacyStatus(AccessModifierType.getKeyfromValue("Class"));
//            setRepository.save(setEntity);
//            classSetRequestRepository.delete(classSetRequestEntity);
//
            return true;
//        }
    }
}
