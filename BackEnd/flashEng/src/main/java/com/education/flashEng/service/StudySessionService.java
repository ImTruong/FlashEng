package com.education.flashEng.service;

import com.education.flashEng.payload.request.StudySessionRequest;

public interface StudySessionService {
    boolean createStudySession(StudySessionRequest studySessionRequest);
}
