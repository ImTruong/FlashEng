package com.education.flashEng.service;

import com.education.flashEng.entity.UserEntity;
import com.education.flashEng.payload.request.RegisterRequest;
import com.education.flashEng.payload.response.ApiResponse;

public interface UserService {
    public boolean register(RegisterRequest registerRequest);
    public UserEntity getUserFromSecurityContext();
}
