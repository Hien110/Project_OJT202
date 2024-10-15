package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.UniClass;

public interface UniClassRepository extends JpaRepository<UniClass, Long>{
    List<UniClass> findBySubject_subjectID(String subjectID);
}
