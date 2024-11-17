package com.example.project_ojt202.controllers;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.Major;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.services.MajorService;
import com.example.project_ojt202.services.StudentProfileService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentProfileController {

    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private MajorService majorService;

    @GetMapping("/allOfStudentList")
    public String listStudents(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, Model model) {
        Page<StudentProfile> studentPage = studentProfileService.getAllStudents(page, size);
        // Lấy tất cả tài khoản sinh viên
        List<Account> studentAccounts = studentProfileService.getStudentAccounts();
        List<Major> majors = majorService.findAllMajor();

        model.addAttribute("majorIds", majors);
        model.addAttribute("accounts", studentAccounts);
        model.addAttribute("students", studentPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        return "allOfStudentList"; // Trang sẽ render
    }

    // Endpoint to view a student's profile details
    @GetMapping("/student/{studentID}")
    public String viewStudentDetails(@PathVariable String studentID, Model model) {
        // Fetch the student profile based on the studentID
        StudentProfile studentProfile = studentProfileService.getStudentProfileById(studentID);
        Account account = studentProfileService.getStudentAccountById(studentID);

        // Add the student profile to the model
        model.addAttribute("studentProfile", studentProfile);
        model.addAttribute("account", account);
        return "studentProfileDetail";  // Return to the view that will display the student details
    }
}
