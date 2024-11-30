package com.example.project_ojt202.controllers;

import java.io.IOException;
import java.util.List;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.AnswerTest;
import com.example.project_ojt202.models.QuestionTest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

        // Get Lecture ID from session
        String lectureID = (account != null && account.getLectureProfile() != null)
                ? account.getLectureProfile().getLectureID()
                : null;

        if (lectureID != null) {
            model.addAttribute("uniClasses", uniClassService.getClassesByLectureID(lectureID));
        } else {
            model.addAttribute("error", "Lecture ID is missing or session is invalid.");
        }

        model.addAttribute("test", new Test());

        List<ScoreTranscript> scoreTranscripts = scoreTranscriptService.findAllScoreTranscripts();
        model.addAttribute("scoreTranscripts", scoreTranscripts);

        return "createTest";
    }

    @PostMapping("/submitTest")
    public String submitTest(
            @ModelAttribute Test test,
            @RequestParam("fileUpload") MultipartFile file,
            Model model) {
        try {
            if (test.isStatusTest()) {
                // Process the uploaded file
                List<QuestionTest> questions = testService.processExcelFile(file);
                List<AnswerTest> answers = testService.processExcelFiles(file);
                // Add the list of questions to the model
                model.addAttribute("questions", questions);
                model.addAttribute("test", test);
                model.addAttribute("answers", answers);
                System.out.println(answers);
                // System.out.println(questions);

                return "submitQuestion";

            } else {
                // Save the test
                testService.saveTest(test);
                model.addAttribute("message", "Test created successfully.");
                return "redirect:/createTest";
            }
        } catch (IOException e) {
            model.addAttribute("error", "Error processing the uploaded file: " + e.getMessage());
            return "errorPage";
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error occurred: " + e.getMessage());
            return "errorPage";
        }
    }
}
