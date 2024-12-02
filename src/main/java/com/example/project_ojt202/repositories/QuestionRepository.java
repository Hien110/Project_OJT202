package com.example.project_ojt202.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.QuestionTest;
import com.example.project_ojt202.models.Subject;


public interface QuestionRepository extends JpaRepository<QuestionTest, Long> {

}
