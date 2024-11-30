package com.example.project_ojt202.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.FeedbackChoice;
import com.example.project_ojt202.repositories.FeedbackChoiceRepository;

import java.util.List;

@Service
public class FeedbackChoiceService {

    @Autowired
    private FeedbackChoiceRepository feedbackChoiceRepository;

    public List<FeedbackChoice> getAllFeedbackChoices() {
        return feedbackChoiceRepository.findAll();
    }

   

    public void deleteFeedbackChoice(Long id) {
        feedbackChoiceRepository.deleteById(id);
    }
    

    public List<FeedbackChoice> getFeedbackChoicesByFeedbackId(Long feedbackID) {
        return feedbackChoiceRepository.findByFeedback_FeedbackID(feedbackID);
    }
    
    public FeedbackChoice getFeedbackChoiceById(Long id) {
        return feedbackChoiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("FeedbackChoice not found with ID: " + id));
    }
    public void updateDChoices(List<FeedbackChoice> choices) {
        for (FeedbackChoice choice : choices) {
            FeedbackChoice existingChoice = feedbackChoiceRepository.findById(choice.getFeedbackChoiceID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid choice ID: " + choice.getFeedbackChoiceID()));
            existingChoice.setFeedbackChoiceContent(choice.getFeedbackChoiceContent());
            existingChoice.setFeedbackChoiceNote(choice.getFeedbackChoiceNote());
            existingChoice.setScore(choice.getScore());
            feedbackChoiceRepository.save(existingChoice);
        }
}
public List<FeedbackChoice> getFeedbackChoicesByIds(List<Long> feedbackChoiceIDs) {
    return feedbackChoiceRepository.findAllById(feedbackChoiceIDs);
}
    
public FeedbackChoice saveFeedbackChoice(FeedbackChoice feedbackChoice) {
    // Kiểm tra nếu điểm số đã được sử dụng
    boolean isScoreUsed = feedbackChoiceRepository.existsByFeedback_FeedbackIDAndScore(
        feedbackChoice.getFeedback().getFeedbackID(),
        feedbackChoice.getScore()
    );

    if (isScoreUsed) {
        throw new IllegalArgumentException("Điểm số đã được sử dụng. Vui lòng chọn điểm khác.");
    }

    return feedbackChoiceRepository.save(feedbackChoice);
}

}