package com.example.project_ojt202.repositories;

import com.example.project_ojt202.models.PrerequisiteSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrerequisiteSubjectRepository extends JpaRepository<PrerequisiteSubject, Long> {
    List<PrerequisiteSubject> findByPrerequisiteSubjectID(String prerequisiteSubjectID);
}
