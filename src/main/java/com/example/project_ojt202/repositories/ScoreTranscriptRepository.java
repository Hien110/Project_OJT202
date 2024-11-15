package com.example.project_ojt202.repositories;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import com.example.project_ojt202.models.ScoreTranscript;

public interface ScoreTranscriptRepository extends JpaRepository<ScoreTranscript, Long> {
    List<ScoreTranscript> findAll();

}