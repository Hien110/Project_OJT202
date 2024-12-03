package com.example.project_ojt202.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.Subject;
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
    //minh
    public List<UniClass> getAllUniClasses() {
        return uniClassRepository.findAll();  
    }
    public UniClass getUniClassByIds(Long uniClassId) {
        Optional<UniClass> uniClassOptional = uniClassRepository.findById(uniClassId);
        return uniClassOptional.orElse(null); // Trả về null nếu không tìm thấy UniClass
    }
    public List<UniClass> getUniClassesBySemester(String semester) {
        return uniClassRepository.findBySemester(semester);
    }
    public List<UniClass> getClassesByLecturer(LectureProfile lectureProfile) {
        return uniClassRepository.findByLectureProfile(lectureProfile);
    }
    public List<UniClass> getClassesByLecturerAndSemester(LectureProfile lectureProfile, String semester) {
        return uniClassRepository.findByLectureProfileAndSemester(lectureProfile, semester);
    }
    
    public List<String> getAllSemesters() {
        return uniClassRepository.findDistinctSemesters();
    }
    // Linh
    public List<UniClass> getClassesByLectureID(String lectureID) {
        return uniClassRepository.findByLectureProfileLectureID(lectureID);
    }

    public void saveUniClass(UniClass uniClass) {
        uniClassRepository.save(uniClass);
    }

    public UniClass findById(Long id) {
        return uniClassRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UniClass not found with ID: " + id));
    }
//
    public UniClass getUniClassById(Long id) {
        return uniClassRepository.findById(id).orElseThrow(() -> new RuntimeException("UniClass not found"));
    }

    public List<String> getDistinctSemesters() {
        return uniClassRepository.findDistinctSemesters();
    }

    @Transactional
    public void deleteUniClass(String subjectID) {
        uniClassRepository.deleteBySubject_SubjectID(subjectID);
    } 
    public String getSubjectIdByUniClassId(Long uniClassId) {
        UniClass uniClass = uniClassRepository.findById(uniClassId).orElse(null);
        return (uniClass != null && uniClass.getSubject() != null) ? uniClass.getSubject().getSubjectID() : null;
    }

    //H.anh
    // Hàm lấy danh sách UniClass theo danh sách Subject
    public List<UniClass> getUniClassesBySubjects(List<Subject> subjects) {
        return uniClassRepository.findBySubjectIn(subjects);
    }
}
