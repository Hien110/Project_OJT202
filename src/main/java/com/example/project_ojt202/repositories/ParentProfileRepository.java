package com.example.project_ojt202.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.ParentProfile;

public interface ParentProfileRepository extends JpaRepository<ParentProfile, String>{
    ParentProfile findByParentID(String parentID);
}
