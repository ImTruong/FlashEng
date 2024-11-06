package com.education.flashEng.repository;

import com.education.flashEng.entity.SetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SetRepository extends JpaRepository<SetEntity, Long> {
    List<SetEntity> findAllByPrivacyStatus(String privacyStatus);

}
