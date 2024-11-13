package com.example.project_ojt202.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.LectureProfileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/lecture")
public class ClassesController {

    private final LectureProfileService lecturerService;

    public ClassesController(LectureProfileService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping("/l-list-classes")
    public String getClasses(@RequestParam(required = false) String semester, Model model, HttpSession session) {
        String lectureID = (String) session.getAttribute("lectureID"); 
        // String lectureID = "L001";
        List<UniClass> classes;

        if (semester != null && !semester.isEmpty()) {
            classes = lecturerService.filterClassesBySemester(lectureID, semester);
        } else {
            classes = lecturerService.getClassesForLecturer(lectureID);
        }

        model.addAttribute("classes", classes);
        return "l_list-classes";
    }

    @GetMapping("/l_list-classes/{uniClassId}/l_list-students")
    public String getStudentsInClass(@PathVariable String uniClassId, Model model) {
        Long uniClassIdLong;
        try {
            uniClassIdLong = Long.valueOf(uniClassId);
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid class ID");
            return "error"; 
        }

        List<StudentProfile> students = lecturerService.getStudentsForClass(uniClassIdLong);
        model.addAttribute("students", students);
        return "l_list-students"; 
    }
}