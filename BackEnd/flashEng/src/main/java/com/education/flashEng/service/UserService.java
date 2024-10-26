package com.education.flashEng.service;

import com.education.flashEng.payload.request.RegisterRequest;
import com.education.flashEng.payload.response.ApiResponse;

public interface UserService {
    public ApiResponse<String> register(RegisterRequest registerRequest);
}
