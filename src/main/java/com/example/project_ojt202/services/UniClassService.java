package com.example.project_ojt202.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.repositories.UniClassRepository;

@Service
public class UniClassService {
    private final UniClassRepository uniClassRepository;

    public UniClassService(UniClassRepository uniClassRepository) {
        this.uniClassRepository = uniClassRepository;
    }

    public List<UniClass> getUniClassBySubjectID(String subjectID) {
        List<UniClass> uniClasses = uniClassRepository.findBySubject_subjectID(subjectID);
        return uniClasses;
    }

    // Linh
    public List<UniClass> getClassesByLectureID(String lectureID) {
        return uniClassRepository.findByLectureProfileLectureID(lectureID);
    }
}
