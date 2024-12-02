package com.example.project_ojt202.services;

import com.example.project_ojt202.repositories.ExamRepository;

public class ExamService {
    private final ExamRepository examRepository;

    public ExamService (ExamRepository examRepository){
        this.examRepository = examRepository;
    }


}
