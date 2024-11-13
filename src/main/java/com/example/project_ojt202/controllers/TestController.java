package com.example.project_ojt202.controllers;

import java.util.List;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.ScoreTranscript;
import com.example.project_ojt202.models.Test;
import com.example.project_ojt202.services.TestService;
import com.example.project_ojt202.services.ScoreTranscriptService;
import com.example.project_ojt202.services.UniClassService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TestController {

    private final UniClassService uniClassService;
    private final TestService testService;
    private final ScoreTranscriptService scoreTranscriptService;

    public TestController(UniClassService uniClassService, TestService testService,
            ScoreTranscriptService scoreTranscriptService) {
        this.uniClassService = uniClassService;
        this.testService = testService;
        this.scoreTranscriptService = scoreTranscriptService;

    }

    @GetMapping("/createTest")
    public String createTestPage(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        String lectureID = null;

        if (account != null && account.getLectureProfile() != null) {
            lectureID = account.getLectureProfile().getLectureID();
        }

        // Kiểm tra nếu lectureID không null
        if (lectureID != null) {
            model.addAttribute("uniClasses", uniClassService.getClassesByLectureID(lectureID));
        } else {
            model.addAttribute("error", "Lecture ID is missing");
        }

        model.addAttribute("test", new Test());

        List<ScoreTranscript> scoreTranscripts = scoreTranscriptService.findAllScoreTranscripts();
        model.addAttribute("scoreTranscripts", scoreTranscripts);

        return "createTest";
    }

    @PostMapping("/submitTest")
    public String submitTest(
            @ModelAttribute Test test,
            Model model) {
        testService.saveTest(test);
        return "createTest";
    }
}
