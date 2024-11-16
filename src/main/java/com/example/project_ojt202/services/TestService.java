package com.example.project_ojt202.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.Test;
import com.example.project_ojt202.repositories.TestRepository;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    public Test saveTest(Test test) {
        return testRepository.save(test);
    }
}
