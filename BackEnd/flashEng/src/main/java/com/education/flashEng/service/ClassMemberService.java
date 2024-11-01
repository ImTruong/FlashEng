package com.education.flashEng.service;

import com.education.flashEng.entity.ClassMemberEntity;
import com.education.flashEng.payload.request.CreateClassRequest;

public interface ClassMemberService {
    public void saveClassMember(ClassMemberEntity classMemberEntity);
}
