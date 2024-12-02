package com.example.project_ojt202.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.example.project_ojt202.models.QuestionTest;
import com.example.project_ojt202.models.Subject;

public interface QuestionTestRepository extends JpaRepository<QuestionTest, Long> {
    @Query("SELECT DISTINCT qt.questionChapter FROM QuestionTest qt WHERE qt.subject = :subject")
    List<Integer> findDistinctChaptersBySubject(@Param("subject") Subject subject);

    @Query("SELECT qt FROM QuestionTest qt " +
    "JOIN qt.lectureProfile lp " +
    "JOIN qt.subject s " +
    "WHERE lp.lectureID = :lectureID AND s.subjectID = :subjectID " +
    "ORDER BY qt.questionTestID DESC")
List<QuestionTest> findTopQuestionTests(@Param("lectureID") String lectureID, 
                                     @Param("subjectID") String subjectID, 
                                     Pageable pageable);;


                                     
}
