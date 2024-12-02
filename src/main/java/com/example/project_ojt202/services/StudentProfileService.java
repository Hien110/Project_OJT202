package com.example.project_ojt202.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.repositories.AccountRepository;
import com.example.project_ojt202.repositories.StudentProfileRepository;

@Service
public class StudentProfileService {
    private final StudentProfileRepository studentProfileRepository;

    @Autowired
    private AccountRepository accountRepository;

    public StudentProfileService(StudentProfileRepository studentProfileRepository) {
        this.studentProfileRepository = studentProfileRepository;
    }

    public StudentProfile getStudentProfileByStudentID(String studentID) {
        StudentProfile student = studentProfileRepository.findByStudentID(studentID);
        return student;
    }

    public Page<StudentProfile> getAllStudents(int page, int size) {
        return studentProfileRepository.findAll(PageRequest.of(page, size));
    }

    // Lấy tất cả các tài khoản với accountRole là 'student'
    public List<Account> getStudentAccounts() {
        return accountRepository.findByAccountRole("student");
    }

    public StudentProfile getStudentProfileById(String studentID) {
        return studentProfileRepository.findById(studentID).orElse(null);
    }

    public Account getStudentAccountById(String studentID) {
        return accountRepository.findByAccountID(studentID);
    }


    public void save(StudentProfile studentProfile) {
        studentProfileRepository.save(studentProfile);
    }

    public void updateStudentProfile(StudentProfile studentProfile) {
        // Thực hiện logic lưu dữ liệu (ví dụ: JPA hoặc JDBC)
        studentProfileRepository.save(studentProfile);
    }    
    public void updateAvatar(String studentID, String avatarUrl) {
        StudentProfile studentProfile = studentProfileRepository.findById(studentID)
            .orElseThrow(() -> new IllegalArgumentException("Student không tồn tại!"));
        studentProfile.setAvatar(avatarUrl);
        studentProfileRepository.save(studentProfile);
    }    
}
