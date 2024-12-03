package com.example.project_ojt202.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.AnswerTest;
import com.example.project_ojt202.models.Exam;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.QuestionTest;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.Test;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.repositories.ExamRepository;
import com.example.project_ojt202.repositories.StudentProfileRepository;
import com.example.project_ojt202.services.AnswerTestService;
import com.example.project_ojt202.services.ExamService;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.services.QuestionTestService;
import com.example.project_ojt202.services.StudentProfileService;
import com.example.project_ojt202.services.TestService;

import jakarta.servlet.http.HttpSession;

@Controller
public class JoinTestController {

    private final TestService testService;
    private final QuestionTestService questionTestService;
    private final AnswerTestService answerTestService;
    private final StudentProfileService studentProfileService;
    private final ExamRepository examRepository;

    public JoinTestController(TestService testService, QuestionTestService questionTestService,
            AnswerTestService answerTestService, StudentProfileService studentProfileService,
            ExamRepository examRepository) {
        this.testService = testService;
        this.questionTestService = questionTestService;
        this.answerTestService = answerTestService;
        this.studentProfileService = studentProfileService;
        this.examRepository = examRepository;
    }

    @GetMapping("joinTest/{testID}")
    public String getTestById(@PathVariable String testID, Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        String studentId = (account != null && account.getStudentProfile() != null)
                ? account.getStudentProfile().getStudentID()
                : null;

        StudentProfile student = studentProfileService.getStudentProfileById(studentId);

        Test test = testService.getAllById(Long.parseLong(testID));

        boolean isTestTaken = examRepository.existsByStudentProfileAndTest(student, test);

        model.addAttribute("test", test);
        model.addAttribute("isTestTaken", isTestTaken);
        if (isTestTaken) {
            model.addAttribute("error", "Bạn đã làm bài kiểm tra này rồi.");
        }

        return "s_JoinTest";
    }

    @PostMapping("/doTest")
    public String doTest(@RequestParam String pass, @RequestParam String testID, Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");

        // Get Lecture ID from session
        String studentId = (account != null && account.getStudentProfile() != null)
                ? account.getStudentProfile().getStudentID()
                : null;
        StudentProfile student = studentProfileService.getStudentProfileById(studentId);
        Test test = testService.getAllById(Long.parseLong(testID));

        UniClass uniClass = test.getUniClass();
        LectureProfile lectureProfile = uniClass.getLectureProfile();

        int questionLimit = test.getQuestionNumber();

        Subject subject = test.getUniClass().getSubject();
        String SubjectId = subject.getSubjectID();
        String LecturerId = lectureProfile.getLectureID();

        if (pass.equals(test.getTestkeyWord())) {

            Map<QuestionTest, List<AnswerTest>> questionAnswerMap = new HashMap<>();
            Exam exam = new Exam();
            exam.setStudentProfile(student);
            exam.setTest(test);

            examRepository.save(exam);
            if (test.isStatusTest()) {
                List<QuestionTest> latestQuestions = questionTestService.getTopQuestionTests(LecturerId, SubjectId,
                        questionLimit);
                for (QuestionTest que : latestQuestions) {

                    List<AnswerTest> answers = answerTestService.getAnswerTestsByQuestionTest(que.getQuestionTestID());

                    questionAnswerMap.put(que, answers);
                }

                int testDurationInMinutes = test.getTime();
                long testDurationInMillis = testDurationInMinutes * 60 * 1000L;

                long currentTime = System.currentTimeMillis();
                long endTime = currentTime + testDurationInMillis;
                System.out.println(exam);
                model.addAttribute("exam", exam);
                model.addAttribute("endTime", endTime);
                model.addAttribute("test", test);
                model.addAttribute("latestQuestions", latestQuestions);
                model.addAttribute("questionAnswerMap", questionAnswerMap);
            } else {
                List<QuestionTest> latestQuestions = questionTestService.getRandomQuestionsForTest(LecturerId,
                        SubjectId, test.getEasyQuestion(), test.getMediumQuestion(), test.getHardQuestion());
                for (QuestionTest que : latestQuestions) {

                    List<AnswerTest> answers = answerTestService.getAnswerTestsByQuestionTest(que.getQuestionTestID());

                    questionAnswerMap.put(que, answers);
                }
                int testDurationInMinutes = test.getTime();
                long testDurationInMillis = testDurationInMinutes * 60 * 1000L;

                long currentTime = System.currentTimeMillis();
                long endTime = currentTime + testDurationInMillis;
                System.out.println(exam);
                model.addAttribute("endTime", endTime);
                model.addAttribute("test", test);
                model.addAttribute("latestQuestions", latestQuestions);
                model.addAttribute("questionAnswerMap", questionAnswerMap);
                model.addAttribute("exam", exam);
            }

            return "test";
        } else {
            model.addAttribute("error", "Mật khẩu sai. Vui lòng thử lại.");
            return "errorPage";
        }

    }
}