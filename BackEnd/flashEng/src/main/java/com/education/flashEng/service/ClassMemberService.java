package com.education.flashEng.service;

import com.education.flashEng.entity.ClassMemberEntity;

public interface ClassMemberService {
    ClassMemberEntity saveClassMember(ClassMemberEntity classMemberEntity);

    ClassMemberEntity getClassMemberByClassIdAndUserId(Long classId, Long userId);

    boolean deleteClassMember(Long userId,Long classId);

    boolean changeRole(Long userId, Long classId, String role);
}
