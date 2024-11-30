package com.example.project_ojt202.services;

import java.util.List;
import java.util.Optional;

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
    //minh
    public List<UniClass> getAllUniClasses() {
        return uniClassRepository.findAll();  
    }
    public UniClass getUniClassByIds(Long uniClassId) {
        Optional<UniClass> uniClassOptional = uniClassRepository.findById(uniClassId);
        return uniClassOptional.orElse(null); // Trả về null nếu không tìm thấy UniClass
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
