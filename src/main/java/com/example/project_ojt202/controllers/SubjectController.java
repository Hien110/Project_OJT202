package com.example.project_ojt202.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Major;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.services.MajorService;
import com.example.project_ojt202.services.SubjectService;

@Controller
public class SubjectController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private MajorService majorService;


     @GetMapping("/allOfSubject")
    public String listStudents(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, Model model) {
        Page<Subject> subjectPage = subjectService.getAllSubjects(page, size);
        List<Major> majors = majorService.findAllMajor();

        model.addAttribute("majorIds", majors);
        model.addAttribute("subjects", subjectPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", subjectPage.getTotalPages());
        return "allOfSubject"; // Trang sẽ render
    }

    // @GetMapping("/lecturer/{lecturerID}")
    // public String viewStudentDetails(@PathVariable String lecturerID, Model model) {
    //     // Fetch the student profile based on the studentID
    //     LectureProfile lecturerProfile = lectureProfileService.getLecProfileByLectureID(lecturerID);
    //     Account account = lectureProfileService.getLecturerAccountById(lecturerID);

    //     // Add the student profile to the model
    //     model.addAttribute("lecturerProfile", lecturerProfile);
    //     model.addAttribute("account", account);
    //     return "lecturerProfileDetail";  // Return to the view that will display the student details
    // }
}

