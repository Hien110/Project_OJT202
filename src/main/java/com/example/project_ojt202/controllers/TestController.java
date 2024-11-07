package com.example.project_ojt202.controllers;

import java.util.List;

import com.example.project_ojt202.models.ScoreTranscript;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.Test;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.repositories.SubjectRepository;
import com.example.project_ojt202.services.TestService;
import com.example.project_ojt202.services.ScoreTranscriptService;
import com.example.project_ojt202.services.SubjectService;
import com.example.project_ojt202.services.UniClassService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String createTestPage(@RequestParam(required = false) String lectureID,
            @RequestParam(required = false) String shortUniClassId, Model model) {

        lectureID = (lectureID != null && !lectureID.isEmpty()) ? lectureID : "L001";
        model.addAttribute("uniClasses", uniClassService.getClassesByLectureID(lectureID));
        model.addAttribute("test", new Test());
        List<ScoreTranscript> scoreTranscripts = scoreTranscriptService.findAllScoreTranscripts();
        model.addAttribute("scoreTranscripts", scoreTranscripts);

        return "createTest";
    }

    @PostMapping("/submitTest")
    public String submitTest(@ModelAttribute Test test,
            Model model) {
        return "createTest"; 
    }
}
