package com.example.project_ojt202.repositories;

import com.example.project_ojt202.models.FeedbackChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackChoiceRepository extends JpaRepository<FeedbackChoice, Long> {
    List<FeedbackChoice> findByFeedback_FeedbackID(Long feedbackID);
    boolean existsByFeedback_FeedbackIDAndScore(Long feedbackID, Integer score);
}
