package com.example.project_ojt202.controllers;

import java.io.IOException;
import java.util.List;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.AnswerTest;
import com.example.project_ojt202.models.QuestionTest;
import com.example.project_ojt202.models.ScoreTranscript;
import com.example.project_ojt202.models.Test;
import com.example.project_ojt202.models.UniClass;
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
import org.springframework.web.bind.annotation.ResponseBody;
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
                if (file.isEmpty()) {
                    model.addAttribute("error", "File is empty. Please upload a valid file.");
                    return "errorPage";
                }

                List<QuestionTest> questions = testService.processExcelFile(file);
                List<AnswerTest> answers = testService.processExcelFiles(file);

                if (questions.isEmpty() || answers.isEmpty()) {
                    model.addAttribute("error", "The uploaded file does not contain valid data.");
                    return "errorPage";
                }

                model.addAttribute("questions", questions);
                model.addAttribute("answers", answers);
                model.addAttribute("test", test);

                return "submitQuestion";

            } else {
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

    //aaaaaaaaaa
    @GetMapping("/getTestsByClassId")
@ResponseBody
public List<ScoreTranscript> getTestsByClassId(@RequestParam Long classId) {
    // Giả sử bạn có đối tượng uniClass để lấy subjectID từ lớp
    UniClass uniClass = uniClassService.findById(classId);
    String subjectId = uniClass.getSubject().getSubjectID();

    // Lọc các bài kiểm tra dựa trên subjectID
    List<ScoreTranscript> scoreTranscripts = scoreTranscriptService.getScoreTranscriptsBySubjectId(subjectId);

    // Trả về dữ liệu dưới dạng JSON
    return scoreTranscripts;
}

}
