package com.example.project_ojt202.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.AnswerTest;
import com.example.project_ojt202.repositories.AnswerRepository;

@Service
public class AnswerTestService {
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerTestService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public AnswerTest saveAnswerTest(AnswerTest answerTest) {
        return answerRepository.save(answerTest);
    }

}
