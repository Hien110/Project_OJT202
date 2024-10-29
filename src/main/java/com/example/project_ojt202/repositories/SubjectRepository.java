package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.Subject;

public interface SubjectRepository extends JpaRepository<Subject, String> {
    List<Subject> findByMajor_MajorIDAndTernNo(String majorID, int ternNo);
}