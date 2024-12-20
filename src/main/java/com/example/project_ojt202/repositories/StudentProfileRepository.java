package com.example.project_ojt202.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project_ojt202.models.StudentProfile;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, String> {

    // Tìm kiếm thông tin sinh viên qua studentID
    StudentProfile findByStudentID(String studentID);

    @SuppressWarnings("null")
    Page<StudentProfile> findAll(@SuppressWarnings("null") Pageable pageable);
}
