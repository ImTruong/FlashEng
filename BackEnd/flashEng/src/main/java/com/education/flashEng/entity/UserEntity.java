package com.education.flashEng.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter

@Entity
@Table(name = "users")

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "status", nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "userEntity")
    private List<SetEntity> setsEntityList;

    @OneToMany(mappedBy = "userEntity")
    private List<NotificationEntity> notificationsEntityList;

    @OneToMany(mappedBy = "inviterEntity")
    private List<ClassInvitationEntity> sentClassInvitations;

    @OneToMany(mappedBy = "inviteeEntity")
    private List<ClassInvitationEntity> receivedClassInvitations;

    @OneToMany(mappedBy = "userEntity")
    private List<ClassSetRequest> classSetRequestList;

    @OneToMany(mappedBy = "userEntity")
    private List<StudySessionEntity> studySessionEntityList;

    @OneToMany(mappedBy = "userEntity")
    private List<ClassMemberEntity> classMemberEntityList;
}
