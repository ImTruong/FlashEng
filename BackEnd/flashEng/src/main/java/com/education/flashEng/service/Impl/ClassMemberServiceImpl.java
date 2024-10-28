package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.ClassMemberEntity;
import com.education.flashEng.repository.ClassMemberRepository;
import com.education.flashEng.service.ClassMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassMemberServiceImpl implements ClassMemberService {

    @Autowired
    private ClassMemberRepository classMemberRepository;

    public void saveClassMember(ClassMemberEntity classMemberEntity) {
        classMemberRepository.save(classMemberEntity);
    }

}
