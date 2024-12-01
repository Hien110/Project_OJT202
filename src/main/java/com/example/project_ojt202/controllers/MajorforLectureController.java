package com.example.project_ojt202.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.services.LectureProfileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/lectures")
public class MajorforLectureController {

    private final LectureProfileService lectureProfileService;

    public MajorforLectureController(LectureProfileService lectureProfileService) {
        this.lectureProfileService = lectureProfileService;
    }

    @GetMapping("/l_lectures-by-major/{majorID}")
    public String listLecturersByMajor(
                                       @PathVariable("majorID") String subjectID,
                                       HttpSession session, Model model) {
        // Retrieve the Account object from the session
        Account account = (Account) session.getAttribute("account");
        String lectureID = null; 
        
        // String lectureID = "L001"; // Optional: use a fixed value or retrieve from session
        if (account != null && account.getLectureProfile() != null) {
            lectureID = account.getLectureProfile().getLectureID();
        }

        // Retrieve the list of lecturers by subject
        List<LectureProfile> lecturers = lectureProfileService.getLecturersByMajor(subjectID);
        
        model.addAttribute("lecturers", lecturers);
        model.addAttribute("lectureID", lectureID);  // Add lectureID to the model if needed
        return "l_lectures-by-major"; // View name to render the list of lecturers
    }    
    @GetMapping("/l_viewLectureProfile/{lectureID}")
    public String getLectureProfile(@PathVariable("lectureID") String lectureID, Model model) {
        LectureProfile lecturer = lectureProfileService.findByLectureID(lectureID);
        System.out.println("aaaaaaaaaaaaa"+lecturer);
        model.addAttribute("lecturer", lecturer);
        return "l_viewLectureProfile"; // Tên của file HTML hiển thị thông tin giảng viên
    }
}

  