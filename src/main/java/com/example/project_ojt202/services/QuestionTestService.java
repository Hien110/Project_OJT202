package com.example.project_ojt202.services;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.QuestionTest;
import com.example.project_ojt202.models.Subject;
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
    
}
