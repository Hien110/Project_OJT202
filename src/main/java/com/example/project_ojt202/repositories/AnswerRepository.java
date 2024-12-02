package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.project_ojt202.models.AnswerTest;

public interface AnswerRepository extends JpaRepository<AnswerTest, Long> {
    List<AnswerTest> findByQuestionTestQuestionTestID(Long questionTestID);

    List<AnswerTest> findByAnswerTestID(Long answerTestID);
}
