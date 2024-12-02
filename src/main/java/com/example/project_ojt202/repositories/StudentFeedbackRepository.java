package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.project_ojt202.models.StudentFeedback;

@Repository
public interface StudentFeedbackRepository extends JpaRepository<StudentFeedback, Long> {
    boolean existsByStudentProfile_StudentIDAndUniClass_UniClassId(String studentID, Long uniClassId);
    List<StudentFeedback> findByStudentProfile_StudentIDAndUniClass_UniClassId(String studentId, Long uniClassId);
    
}

