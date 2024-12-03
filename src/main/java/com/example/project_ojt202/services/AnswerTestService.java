package com.example.project_ojt202.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.AnswerTest;
import com.example.project_ojt202.repositories.AnswerRepository;

@Service
public class AnswerTestService {
    private final AnswerRepository answerRepository;

    public AnswerTestService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public AnswerTest saveAnswerTest(AnswerTest answerTest) {
        return answerRepository.save(answerTest);
    }

    public List<AnswerTest> getAnswerTestsByQuestionTest(Long questionTestID) {
        return answerRepository.findByQuestionTestQuestionTestID(questionTestID);
    }

    public boolean getAnswerTestTrueByAnswerTestID(Long answerTestID) {
        List<AnswerTest> answerTests = answerRepository.findByAnswerTestID(answerTestID);
        if (!answerTests.isEmpty()) {
            return answerTests.get(0).isAnswerTestTrue(); // Trả về answerTestTrue của AnswerTest đầu tiên
        }
        return false; // Nếu không tìm thấy, giả sử câu trả lời không đúng
    }

}
