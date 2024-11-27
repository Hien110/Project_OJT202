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

    public void save(StudentProfile studentProfile) {
        studentProfileRepository.save(studentProfile);
    }

    public void updateStudentProfile(StudentProfile studentProfile) {
        // Thực hiện logic lưu dữ liệu (ví dụ: JPA hoặc JDBC)
        studentProfileRepository.save(studentProfile);
    }    

    public void updateAvatar(String studentID, String avatarPath) {
        StudentProfile profile = studentProfileRepository.findById(studentID)
                              .orElseThrow(() -> new RuntimeException("Student not found"));
        profile.setAvatar(avatarPath);
        studentProfileRepository.save(profile);
    }

}
