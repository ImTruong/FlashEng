package com.education.flashEng.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "role_class")
public class RoleClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "roleClassEntity")
    private List<ClassMemberEntity> classMemberEntityList;
}
