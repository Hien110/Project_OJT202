package com.example.project_ojt202.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.project_ojt202.models.QuestionTest;

public interface QuestionRepository extends JpaRepository<QuestionTest, Long> {
}
