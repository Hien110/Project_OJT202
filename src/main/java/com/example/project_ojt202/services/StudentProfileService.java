package com.example.project_ojt202.services;

import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.repositories.StudentProfileRepository;

@Service
public class StudentProfileService {
    private final StudentProfileRepository studentProfileRepository;

    public StudentProfileService(StudentProfileRepository studentProfileRepository ){
        this.studentProfileRepository = studentProfileRepository;
    }

    public StudentProfile getStudentProfileByStudentID(String studentID){
        StudentProfile student = studentProfileRepository.findByStudentID(studentID);
        return student;
    }
}
