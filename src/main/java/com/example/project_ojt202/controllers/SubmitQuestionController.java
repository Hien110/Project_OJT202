package com.example.project_ojt202.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.AnswerTest;
import com.example.project_ojt202.models.QuestionTest;
import com.example.project_ojt202.models.Test;
import com.example.project_ojt202.services.AnswerTestService;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.services.QuestionTestService;
import com.example.project_ojt202.services.ScoreTranscriptService;
import com.example.project_ojt202.services.SubjectService;
import com.example.project_ojt202.services.TestService;
import com.example.project_ojt202.services.UniClassService;
import com.example.project_ojt202.models.Subject;

import jakarta.servlet.http.HttpSession;

@Controller
public class SubmitQuestionController {
    private final TestService testService;
    private final QuestionTestService questionTestService;
    private final AnswerTestService answerTestService;
    private final UniClassService uniClassService;
    private final ScoreTranscriptService scoreTranscriptService;
    private final LectureProfileService lectureProfileService;
    private final SubjectService subjectService;

    public SubmitQuestionController(TestService testService,
            QuestionTestService questionTestService,
            AnswerTestService answerTestService,
            UniClassService uniClassService,
            ScoreTranscriptService scoreTranscriptService,
            LectureProfileService lectureProfileService,
            SubjectService subjectService) {
        this.testService = testService;
        this.questionTestService = questionTestService;
        this.answerTestService = answerTestService;
        this.uniClassService = uniClassService;
        this.scoreTranscriptService = scoreTranscriptService;
        this.lectureProfileService = lectureProfileService;
        this.subjectService = subjectService;
    }

    @PostMapping("createQuestion")
    public String submitTest(

            @RequestParam String testStartTime,
            @RequestParam String testFinishTime,
            @RequestParam String testkeyWord,
            @RequestParam String testLevel,
            @RequestParam int questionNumber,
            @RequestParam int easyQuestion,
            @RequestParam List<String> questionChapter,
            @RequestParam List<String> questionLevel,
            @RequestParam List<String> questionTestContent,
            @RequestParam int mediumQuestion,
            @RequestParam int hardQuestion,
            @RequestParam boolean statusTest,
            @RequestParam Long uniClassId,
            @RequestParam Long scoreTranscriptID,
            HttpSession session,
            @RequestParam List<String> answerTestContent,
            @RequestParam String testName,
            @RequestParam int time,
            @RequestParam List<Boolean> isCorrect,
            Model model) {

        Account account = (Account) session.getAttribute("account");

        String lectureID = (account != null && account.getLectureProfile() != null)
                ? account.getLectureProfile().getLectureID()
                : null;

        Test test = new Test();
        try {
            test.setTestStartTime(LocalDateTime.parse(testStartTime));
            test.setTestFinishTime(LocalDateTime.parse(testFinishTime));
        } catch (Exception e) {
            model.addAttribute("error", "Invalid date format for Test Start/Finish Time.");
            return "errorPage";
        }
        test.setTestkeyWord(testkeyWord);
        test.setTestLevel(testLevel);
        test.setQuestionNumber(questionNumber);
        test.setEasyQuestion(easyQuestion);
        test.setMediumQuestion(mediumQuestion);
        test.setHardQuestion(hardQuestion);
        test.setStatusTest(statusTest);
        test.setExamName(testName);
        test.setTime(time);
        test.setUniClass(uniClassService.findById(uniClassId));
        test.setScoreTranscript(scoreTranscriptService.findById(scoreTranscriptID));

        testService.saveTest(test);

        for (int i = 0; i < questionTestContent.size(); i++) {
            try {
                String SubjectId = uniClassService.getSubjectIdByUniClassId(uniClassId);
                QuestionTest questionTest = new QuestionTest();
                questionTest.setQuestionTestContent(questionTestContent.get(i));
                questionTest.setQuestionChapter(Integer.parseInt(questionChapter.get(i)));
                questionTest.setQuestionLevel(questionLevel.get(i));
                questionTest.setLectureProfile(lectureProfileService.findByLectureID(lectureID));
                questionTest.setSubject(subjectService.getSubjectById(SubjectId));
                questionTestService.saveQuestionTest(questionTest);

                int answerStartIndex = i * 4;
                int answerEndIndex = (i + 1) * 4;

                System.out.println(
                        "Question " + i + " has answers from index " + answerStartIndex + " to " + answerEndIndex);
                System.out.println("Total isCorrect size: " + isCorrect.size());

                int numberOfAnswersForThisQuestion = answerEndIndex - answerStartIndex;
                if (answerTestContent.size() < numberOfAnswersForThisQuestion
                        || isCorrect.size() < numberOfAnswersForThisQuestion) {
                    model.addAttribute("error",
                            "Mismatch between the number of answers and the number of correctness indicators.");
                    return "errorPage";
                }

                for (int j = answerStartIndex; j < answerEndIndex; j++) {
                    if (j >= answerTestContent.size() || j >= isCorrect.size()) {
                        break;
                    }

                    AnswerTest answerTest = new AnswerTest();
                    answerTest.setAnswerTestContent(answerTestContent.get(j));

                    Boolean correctAnswer = (isCorrect.size() > j) ? isCorrect.get(j) : false;
                    answerTest.setAnswerTestTrue(correctAnswer);

                    answerTest.setQuestionTest(questionTest);
                    answerTestService.saveAnswerTest(answerTest);
                }

            } catch (NumberFormatException e) {
                model.addAttribute("error", "Invalid Chapter Number for Question " + (i));
                return "errorPage";
            } catch (Exception e) {
                model.addAttribute("error", "Error while processing question " + (i) + ": " + e.getMessage());
                return "errorPage";
            }
        }

        model.addAttribute("message", "Test created successfully!");
        return "redirect:/createTest";
    }
}
