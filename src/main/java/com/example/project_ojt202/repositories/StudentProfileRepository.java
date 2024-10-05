package com.example.project_ojt202.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.StudentProfile;


public interface StudentProfileRepository extends JpaRepository<StudentProfile, String>{
    StudentProfile findByStudentID(String studentID);
}
