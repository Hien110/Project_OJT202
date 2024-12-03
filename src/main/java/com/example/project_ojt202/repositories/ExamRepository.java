package com.example.project_ojt202.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.example.project_ojt202.models.Exam;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.models.Test;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    boolean existsByStudentProfileAndTest(StudentProfile studentProfile, Test test);

    Exam findByStudentProfile_StudentIDAndTest_TestID(String studentID, Long testID);

    @Transactional
    @Modifying
    @Query("UPDATE Exam e SET e.examScore = :examScore WHERE e.examID = :examID")
    int updateExamScore(Long examID, Long examScore);

    @Query("SELECT e.test.testID FROM Exam e WHERE e.examID = :examID")
    Long findTestIdByExamID(Long examID);
}
