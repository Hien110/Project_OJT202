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

        @Query("SELECT q FROM QuestionTest q WHERE q.lectureProfile.lectureID = :lectureId " +
                        "AND q.subject.subjectID = :subjectId " +
                        "AND q.questionLevel = 'hard_question' " +
                        "ORDER BY q.questionTestID")
        List<QuestionTest> findHardQuestions(@Param("lectureId") String lectureId,
                        @Param("subjectId") String subjectId,
                        Pageable pageable);

        @Query(value = "SELECT * FROM questiontest q WHERE q.lectureid = :lectureId " +
                        "AND q.subjectid = :subjectId AND q.question_level = 'hard_question' " +
                        "ORDER BY RAND() LIMIT :limit", nativeQuery = true)
        List<QuestionTest> findRandomHardQuestions(@Param("lectureId") String lectureId,
                        @Param("subjectId") String subjectId,
                        @Param("limit") int limit);

        @Query(value = "SELECT * FROM questiontest q WHERE q.lectureid = :lectureId " +
                        "AND q.subjectid = :subjectId AND q.question_level = 'easy_question' " +
                        "ORDER BY RAND() LIMIT :limit", nativeQuery = true)
        List<QuestionTest> findRandomEasyQuestions(@Param("lectureId") String lectureId,
                        @Param("subjectId") String subjectId,
                        @Param("limit") int limit);

        @Query(value = "SELECT * FROM questiontest q WHERE q.lectureid = :lectureId " +
                        "AND q.subjectid = :subjectId AND q.question_level = 'medium_question' " +
                        "ORDER BY RAND() LIMIT :limit", nativeQuery = true)
        List<QuestionTest> findRandomMediumQuestions(@Param("lectureId") String lectureId,
                        @Param("subjectId") String subjectId,
                        @Param("limit") int limit);

}
