package com.education.flashEng.service;

import com.education.flashEng.entity.UserEntity;
import com.education.flashEng.payload.request.RegisterRequest;
import com.education.flashEng.payload.request.UpdateUserRequest;
import com.education.flashEng.payload.response.ApiResponse;

public interface UserService {
    boolean register(RegisterRequest registerRequest);
    UserEntity getUserFromSecurityContext();
    UserEntity getUserById(Long id);

    boolean update(UpdateUserRequest updateRequest);
}
