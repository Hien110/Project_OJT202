package com.example.project_ojt202.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.SubjectService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Lecturer")
public class ViewSubjectLectureController {

    private final SubjectService subjectService;
    public ViewSubjectLectureController(SubjectService subjectService){
        this.subjectService = subjectService;
    }

    @GetMapping("/l_viewList-subject/{majorID}")
    public String listSubjectsByMajorId(@PathVariable("majorID") String majorID, Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        String lectureID = null;
        if (account != null && account.getLectureProfile() != null) {
            lectureID = account.getLectureProfile().getLectureID();
        }
        List<Subject> subjects = subjectService.getSubjectsByMajorId(majorID);
        model.addAttribute("subjects", subjects);
        return "l_viewList-subject"; // Tên file HTML để hiển thị dữ liệu
    }
}
