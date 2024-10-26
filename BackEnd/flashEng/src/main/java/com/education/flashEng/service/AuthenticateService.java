package com.education.flashEng.service;

import com.education.flashEng.payload.request.LoginRequest;
import com.education.flashEng.payload.response.ApiResponse;

public interface AuthenticateService {
    ApiResponse<String> login(LoginRequest loginRequest);
}
