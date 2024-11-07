package com.example.project_ojt202.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.ScoreTranscript;
import com.example.project_ojt202.repositories.ScoreTranscriptRepository;

@Service
public class ScoreTranscriptService {
    private final ScoreTranscriptRepository scoreTranscriptRepository;

    public ScoreTranscriptService(ScoreTranscriptRepository scoreTranscriptRepository) {
        this.scoreTranscriptRepository = scoreTranscriptRepository;
    }

    public List<ScoreTranscript> findAllScoreTranscripts() {
        return scoreTranscriptRepository.findAll();
    }

}
