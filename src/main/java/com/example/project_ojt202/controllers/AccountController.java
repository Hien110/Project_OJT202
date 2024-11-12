package com.example.project_ojt202.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.project_ojt202.services.StudentProfileService;
import com.example.project_ojt202.models.StudentProfile;

@Controller
public class AccountController {
    
    private final StudentProfileService studentProfileService;

    public AccountController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }

    @GetMapping("/studentProfile/{studentID}")
    public String accountInfo(@PathVariable String studentID, Model model) {
        StudentProfile studentProfile = studentProfileService.getStudentProfileByStudentID(studentID);
        System.out.println("Student ID: " + studentID); // Log để kiểm tra studentID
        System.out.println("Student Profile: " + studentProfile); // Log để kiểm tra studentProfile
        model.addAttribute("studentProfile", studentProfile);
        return "studentProfile"; // Trả về tên của trang HTML
    }
    
    public String showResetPasswordPage() {
        return "resetpassword";
    }
    
}
