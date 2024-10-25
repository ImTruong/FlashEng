package com.education.flashEng.repository;

import com.education.flashEng.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByUsername(String userName);
    UserEntity findByEmail(String email);
}
