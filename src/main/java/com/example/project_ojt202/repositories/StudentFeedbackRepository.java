package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.project_ojt202.models.StudentFeedback;


@Repository
public interface StudentFeedbackRepository extends JpaRepository<StudentFeedback, Long> {
    boolean existsByStudentProfile_StudentIDAndUniClass_UniClassId(String studentID, Long uniClassId);
    List<StudentFeedback> findByStudentProfile_StudentIDAndUniClass_UniClassId(String studentId, Long uniClassId);
    @Query("SELECT sf FROM StudentFeedback sf " +
           "JOIN FETCH sf.uniClass uc " +
           "JOIN FETCH sf.feedbackChoice fc " +
           "JOIN FETCH fc.feedback f " +
           "WHERE uc.uniClassId = :classId")
    List<StudentFeedback> findFeedbackByClassId(@Param("classId") Long classId);
    List<StudentFeedback> findByUniClass_UniClassId(Long uniClassId);
}

