package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.ClassEntity;
import com.education.flashEng.entity.ClassMemberEntity;
import com.education.flashEng.entity.RoleClassEntity;
import com.education.flashEng.entity.UserEntity;
import com.education.flashEng.payload.request.CreateClassRequest;
import com.education.flashEng.repository.ClassRepository;
import com.education.flashEng.service.ClassMemberService;
import com.education.flashEng.service.ClassService;
import com.education.flashEng.service.RoleClassService;
import com.education.flashEng.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private RoleClassService roleClassService;

    @Autowired
    private ClassMemberService classMemberService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public boolean createClass(CreateClassRequest createClassRequest) {
        UserEntity user = userService.getUserFromSecurityContext();

        ClassEntity classEntity = modelMapper.map(createClassRequest, ClassEntity.class);
        classRepository.save(classEntity);

        RoleClassEntity adminRole = roleClassService.getRoleClassByName("ADMIN");

        ClassMemberEntity classMemberEntity = ClassMemberEntity.builder()
                .classEntity(classEntity)
                .userEntity(user)
                .roleClassEntity(adminRole)
                .build();

        classMemberService.saveClassMember(classMemberEntity);

        return true;
    }
}
