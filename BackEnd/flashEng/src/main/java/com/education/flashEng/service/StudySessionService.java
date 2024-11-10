package com.education.flashEng.service;

import com.education.flashEng.payload.request.StudySessionRequest;
import com.education.flashEng.payload.response.StatisticResponse;

import java.util.List;

public interface StudySessionService {
    boolean createStudySession(StudySessionRequest studySessionRequest);
    List<StatisticResponse> getDailyWordCountByUserId();
}
