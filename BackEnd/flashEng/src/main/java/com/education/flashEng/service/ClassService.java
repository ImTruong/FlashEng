package com.education.flashEng.service;

import com.education.flashEng.entity.ClassEntity;
import com.education.flashEng.payload.request.CreateClassRequest;
import com.education.flashEng.payload.response.ApiResponse;

import java.util.Optional;

public interface ClassService {
    boolean createClass(CreateClassRequest createClassRequest);

    ClassEntity getClassById(Long id);
    boolean updateClassName(Long classId, String name);
}
