package com.example.project_ojt202.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.QuestionTest;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.Test;
import com.example.project_ojt202.repositories.QuestionTestRepository;
import com.example.project_ojt202.repositories.SubjectRepository;

@Service
public class QuestionTestService {
    private final QuestionTestRepository questionTestRepository;
    private final SubjectRepository subjectRepository;

    public QuestionTestService(QuestionTestRepository questionTestRepository, SubjectRepository subjectRepository) {
        this.questionTestRepository = questionTestRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<Integer> getDistinctChaptersBySubjectId(String subjectID) {
        Subject subject = subjectRepository.findById(subjectID).orElse(null);
        if (subject != null) {
            return questionTestRepository.findDistinctChaptersBySubject(subject);
        }
        return Collections.emptyList();
    }

    public QuestionTest saveQuestionTest(QuestionTest questionTest) {
        return questionTestRepository.save(questionTest);
    }

    public List<QuestionTest> getTopQuestionTests(String lectureID, String subjectID, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return questionTestRepository.findTopQuestionTests(lectureID, subjectID, pageable);
    }

    public List<QuestionTest> getRandomQuestionsForTest(String lectureId, String subjectId, int easyQuestion,
            int mediumQuestion, int hardQuestion) {
        List<QuestionTest> result = new ArrayList<>();

        result.addAll(questionTestRepository.findRandomHardQuestions(lectureId, subjectId, hardQuestion));

        result.addAll(questionTestRepository.findRandomEasyQuestions(lectureId, subjectId, easyQuestion));

        result.addAll(questionTestRepository.findRandomMediumQuestions(lectureId, subjectId, mediumQuestion));

        return result;
    }

    public List<QuestionTest> getRandomQuestionsForTest(String lecturerId, String subjectId, Test test) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRandomQuestionsForTest'");
    }

}
