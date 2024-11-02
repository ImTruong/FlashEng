package com.education.flashEng.repository;

import com.education.flashEng.entity.ClassInvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassInvitationRepository extends JpaRepository<ClassInvitationEntity,Long> {
    Optional<ClassInvitationEntity> findByClassEntityIdAndInviteeEntityIdAndInviterEntityId(Long classId, Long inviteeId, Long inviterId);
}
