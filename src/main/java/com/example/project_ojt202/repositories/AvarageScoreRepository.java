package com.example.project_ojt202.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.AvarageScore;
import com.example.project_ojt202.models.Learn;

public interface AvarageScoreRepository extends JpaRepository<AvarageScore, Long> {
    Optional<AvarageScore> findByLearn(Learn learn);

    AvarageScore findByLearn_LearnID(Long learnID);
}
