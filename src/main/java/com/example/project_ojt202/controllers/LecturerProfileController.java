package com.example.project_ojt202.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.Major;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.services.MajorService;

@Controller
public class LecturerProfileController {

    @Autowired
    private LectureProfileService lectureProfileService;
    @Autowired
    private MajorService majorService;


     @GetMapping("/allOfLecturer")
    public String listStudents(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, Model model) {
        Page<LectureProfile> lecturerPage = lectureProfileService.getAllLecturers(page, size);
        List<Account> lecturerAccounts = lectureProfileService.getLecturerAccounts();
        List<Major> majors = majorService.findAllMajor();

        model.addAttribute("majorIds", majors);
        model.addAttribute("accounts", lecturerAccounts);
        model.addAttribute("lecturers", lecturerPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", lecturerPage.getTotalPages());
        return "allOfLecturer"; // Trang sẽ render
    }

    @GetMapping("/lecturer/{lecturerID}")
    public String viewStudentDetails(@PathVariable String lecturerID, Model model) {
        // Fetch the student profile based on the studentID
        LectureProfile lecturerProfile = lectureProfileService.getLecProfileByLectureID(lecturerID);
        Account account = lectureProfileService.getLecturerAccountById(lecturerID);

        // Add the student profile to the model
        model.addAttribute("lecturerProfile", lecturerProfile);
        model.addAttribute("account", account);
        return "lecturerProfileDetail";  // Return to the view that will display the student details
    }
}

