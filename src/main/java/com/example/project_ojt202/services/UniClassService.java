package com.example.project_ojt202.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.repositories.UniClassRepository;

import jakarta.transaction.Transactional;

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

    public List<UniClass> getUniClassBySubjectIDAndSemester(String semester, String subjectID) {
        List<UniClass> uniClasses = uniClassRepository.findBySemesterAndSubject_subjectID(semester, subjectID);
        return uniClasses;
    }

    // Linh
    public List<UniClass> getClassesByLectureID(String lectureID) {
        return uniClassRepository.findByLectureProfileLectureID(lectureID);
    }

    public void saveUniClass(UniClass uniClass){
        uniClassRepository.save(uniClass);
    }

    public UniClass getUniClassById(Long id){
        return uniClassRepository.findById(id).orElseThrow(() -> new RuntimeException("UniClass not found"));
    }

    @Transactional
    public void deleteUniClass(String subjectID){
        uniClassRepository.deleteBySubject_SubjectID(subjectID);
    }
}