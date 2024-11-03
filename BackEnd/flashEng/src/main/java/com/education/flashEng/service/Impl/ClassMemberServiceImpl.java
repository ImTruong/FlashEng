package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.ClassMemberEntity;
import com.education.flashEng.entity.UserEntity;
import com.education.flashEng.exception.EntityNotFoundWithIdException;
import com.education.flashEng.repository.ClassMemberRepository;
import com.education.flashEng.service.ClassMemberService;
import com.education.flashEng.service.RoleClassService;
import com.education.flashEng.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class ClassMemberServiceImpl implements ClassMemberService {

    @Autowired
    private ClassMemberRepository classMemberRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleClassService roleClassService;

    public ClassMemberEntity saveClassMember(ClassMemberEntity classMemberEntity) {
        return classMemberRepository.save(classMemberEntity);
    }

    @Override
    public ClassMemberEntity getClassMemberByClassIdAndUserId(Long classId, Long userId) {
        return classMemberRepository.findByClassEntityIdAndUserEntityId(classId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundWithIdException("Class And User", classId + " And " + userId));
    }

    @Transactional
    @Override
    public boolean deleteClassMember(Long userId,Long classId) {
        UserEntity user = userService.getUserFromSecurityContext();
        ClassMemberEntity classMemberEntity = classMemberRepository.findByClassEntityIdAndUserEntityId(classId, userId)
                .orElseThrow(() -> new AccessDeniedException("User is not a member of this class."));
        ClassMemberEntity classCurrentMemberEntity = classMemberRepository.findByClassEntityIdAndUserEntityId(classId, user.getId())
                .orElseThrow(() -> new AccessDeniedException("You are not a member of this class."));
        if (!classCurrentMemberEntity.getRoleClassEntity().getName().equals("ADMIN"))
            throw new AccessDeniedException("You are not authorized to delete members from this class.");
        classMemberRepository.delete(classMemberEntity);
        return false;
    }

    @Transactional
    @Override
    public boolean changeRole(Long userId, Long classId, String role) {
        UserEntity user = userService.getUserFromSecurityContext();
        ClassMemberEntity classMemberEntity = classMemberRepository.findByClassEntityIdAndUserEntityId(classId, user.getId())
                .orElseThrow(() -> new AccessDeniedException("You are not a member of this class."));
        if (!classMemberEntity.getRoleClassEntity().getName().equals("ADMIN"))
            throw new AccessDeniedException("You are not authorized to change roles in this class.");
        ClassMemberEntity memberEntity = classMemberRepository.findByClassEntityIdAndUserEntityId(classId, userId)
                .orElseThrow(() -> new EntityNotFoundWithIdException("Class Member", userId.toString()));
        if (memberEntity.getRoleClassEntity().getName().equals("ADMIN"))
            throw new AccessDeniedException("You can't change the role of an admin.");
        memberEntity.setRoleClassEntity(roleClassService.getRoleClassByName(role.toUpperCase()));
        classMemberRepository.save(memberEntity);
        return true;
    }
}
