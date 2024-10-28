package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.RoleClassEntity;
import com.education.flashEng.exception.EntityNotFoundException;
import com.education.flashEng.repository.RoleClassRepository;
import com.education.flashEng.service.RoleClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleClassServiceImpl implements RoleClassService {

    @Autowired
    private RoleClassRepository roleRepository;

    @Override
    public RoleClassEntity getRoleClassByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role", roleName));
    }
}
