package com.education.flashEng.repository;

import com.education.flashEng.entity.StudySessionEntity;
import com.education.flashEng.payload.request.StudySessionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudySessionRepository extends JpaRepository<StudySessionEntity, Long> {

}
