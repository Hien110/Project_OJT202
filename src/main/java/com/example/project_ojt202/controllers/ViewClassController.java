package com.example.project_ojt202.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.LectureProfileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/lecture")
public class ViewClassController {

    private final LectureProfileService lecturerService;

    public ViewClassController(LectureProfileService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping("/l_viewClass")
    public String getClasses(@RequestParam(required = false) String semester, Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        String lectureID = null; 
        // String lectureID = "L001";
        List<UniClass> classes;
        if (account != null && account.getLectureProfile() != null) {
            lectureID = account.getLectureProfile().getLectureID();
        }

        if (semester != null && !semester.isEmpty()) {
            classes = lecturerService.filterClassesBySemester(lectureID, semester);
            
        } else {
            classes = lecturerService.getClassesForLecturer(lectureID);
        }
        

        model.addAttribute("classes", classes);
        return "l_viewClass";
    }

    @GetMapping("/l_viewClass/{uniClassId}/l_viewStudent")
    public String getStudentsInClass(@PathVariable Long uniClassId, Model model) {
        System.out.println("Class ID: " + uniClassId);
    
        // Sử dụng trực tiếp uniClassId mà không ép kiểu sang Long
        List<StudentProfile> students = lecturerService.getStudentsForClass(uniClassId);
        
        // Kiểm tra nếu không có dữ liệu sinh viên
        if (students == null || students.isEmpty()) {
            model.addAttribute("errorMessage", "Không có sinh viên nào trong lớp");
        } else {
            model.addAttribute("students", students);
        }
        return "l_viewStudent"; 
    }
}