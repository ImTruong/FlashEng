package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.ClassMemberEntity;
import com.education.flashEng.entity.StudySessionEntity;
import com.education.flashEng.entity.UserEntity;
import com.education.flashEng.entity.WordEntity;
import com.education.flashEng.exception.EntityNotFoundWithIdException;
import com.education.flashEng.payload.request.StudySessionRequest;
import com.education.flashEng.repository.StudySessionRepository;
import com.education.flashEng.repository.WordRepository;
import com.education.flashEng.service.StudySessionService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class StudySessionServiceImpl implements StudySessionService {
    private final UserServiceImpl userServiceImpl;
    private final ModelMapper modelMapper;
    private final WordRepository wordRepository;
    private final StudySessionRepository studySessionRepository;
    private final NotificationServiceImpl notificationServiceImpl;

    public StudySessionServiceImpl(UserServiceImpl userServiceImpl, ModelMapper modelMapper, WordRepository wordRepository, StudySessionRepository studySessionRepository, NotificationServiceImpl notificationServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.modelMapper = modelMapper;
        this.wordRepository = wordRepository;
        this.studySessionRepository = studySessionRepository;
        this.notificationServiceImpl = notificationServiceImpl;
    }

    @Override
    public boolean createStudySession(StudySessionRequest studySessionRequest) {
        UserEntity currentUser = userServiceImpl.getUserFromSecurityContext();
        StudySessionEntity studySessionEntity = new StudySessionEntity();
        WordEntity wordEntity = wordRepository.findById(studySessionRequest.getWordId())
                .orElseThrow(() -> new EntityNotFoundWithIdException("Word", studySessionRequest.getWordId().toString()));
        if(Objects.equals(studySessionEntity.getWordEntity().getSetEntity().getPrivacyStatus(), "CLASS")){
            //Bug
            List<ClassMemberEntity> classMemberEntities = studySessionEntity.getWordEntity().getSetEntity().getClassEntity().getClassMemberEntityList();
            for(ClassMemberEntity classMemberEntity : classMemberEntities){
                if(Objects.equals(classMemberEntity.getUserEntity().getId(), currentUser.getId())){
                    studySessionEntity.setUserEntity(currentUser);
                    studySessionEntity.setWordEntity(wordEntity);
                    studySessionEntity.setDifficulty(studySessionRequest.getDifficulty());
                    StudySessionEntity studySession = studySessionRepository.save(studySessionEntity);
                    notificationServiceImpl.createStudySessionNotification(studySession, setReminderTimeBasedOnLevel(studySession.getDifficulty()));
                    return true;
                }
            }
            throw new AccessDeniedException("You do not permission to learning this word");
        }
        else{
            studySessionEntity.setUserEntity(currentUser);
            studySessionEntity.setWordEntity(wordEntity);
            studySessionEntity.setDifficulty(studySessionRequest.getDifficulty());
            StudySessionEntity studySession = studySessionRepository.save(studySessionEntity);
            notificationServiceImpl.createStudySessionNotification(studySession, setReminderTimeBasedOnLevel(studySession.getDifficulty()));
        }
        return true;
    }

    public LocalDateTime setReminderTimeBasedOnLevel(String difficulty) {
        LocalDateTime now = LocalDateTime.now();
        return switch (difficulty) {
            case "Very difficult" -> now.plusHours(3);
            case "Difficult" -> now.plusDays(1);
            case "Easy" -> now.plusDays(3);
            case "Very easy" -> now.plusHours(5);
            default -> throw new IllegalArgumentException("Invalid Difficulty");
        };
    }
}
