package com.education.flashEng.repository;

import com.education.flashEng.entity.ClassMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassMemberRepository extends JpaRepository<ClassMemberEntity,Long> {

}
