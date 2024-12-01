package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.Learn;

public interface LearnRepository extends JpaRepository<Learn, Long> {
    List<Learn> findByUniClass_UniClassId(Long uniClassId);
    List<Learn> findByStudentProfile_StudentID(String studentID);
}