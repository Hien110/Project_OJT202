package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.LectureProfile;

public interface LectureProfileRepository extends JpaRepository<LectureProfile, String>{
    LectureProfile findByLectureID(String lectureID);
    List<LectureProfile> findByMajor_majorID(String majorID);
}
