package com.example.project_ojt202.services;

import com.example.project_ojt202.models.AvarageScore;
import com.example.project_ojt202.models.ScoreTranscript;
import com.example.project_ojt202.repositories.AvarageScoreRepository;
import com.example.project_ojt202.repositories.ScoreTranscriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvarageScoreService {

    @Autowired
    private AvarageScoreRepository avarageScoreRepository;

    @Autowired
    private ScoreTranscriptRepository scoreTranscriptRepository;

    
}
