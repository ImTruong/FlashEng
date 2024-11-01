package com.education.flashEng.service;

import com.education.flashEng.payload.request.CreateClassRequest;
import com.education.flashEng.payload.response.ApiResponse;

public interface ClassService {
    public boolean createClass(CreateClassRequest createClassRequest);
}
