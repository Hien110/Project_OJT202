package com.example.project_ojt202.controllers;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.Exam;
import com.example.project_ojt202.models.QuestionTest;
import com.example.project_ojt202.models.StudentAnswer;
import com.example.project_ojt202.repositories.ExamRepository;
import com.example.project_ojt202.repositories.QuestionTestRepository;
import com.example.project_ojt202.repositories.StudentAnswerRepository;
import com.example.project_ojt202.services.AnswerTestService;
import com.example.project_ojt202.services.ExamService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class SubmitTestController {

    @Autowired
    private ObjectMapper objectMapper;

    private final AnswerTestService answerTestService;
    private final ExamRepository examRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final QuestionTestRepository questionTestRepository;

    // Constructor để inject AnswerTestService
    public SubmitTestController(AnswerTestService answerTestService, ExamRepository examRepository,
            StudentAnswerRepository studentAnswerRepository, QuestionTestRepository questionTestRepository) {
        this.examRepository = examRepository;
        this.answerTestService = answerTestService;
        this.studentAnswerRepository = studentAnswerRepository;
        this.questionTestRepository = questionTestRepository;

    }

    @PostMapping("calculatScore")
    public String submitTest(@RequestParam("selectedAnswers") String selectedAnswersJson,
            @RequestParam("questionNumber") int questionNumber,
            @RequestParam("examName") String examName,
            @RequestParam String examID,
            @RequestParam String testId,
            HttpSession session,
            Model model) {
        Account account = (Account) session.getAttribute("account");

        // Get Lecture ID from session
        String studentId = (account != null && account.getStudentProfile() != null)
                ? account.getStudentProfile().getStudentID()
                : null;

        try {
            // Chuyển đổi selectedAnswers từ JSON thành Map
            Map<String, String> selectedAnswers = objectMapper.readValue(selectedAnswersJson, Map.class);

            // In ra các câu trả lời đã chọn
            System.out.println("Các câu trả lời đã chọn:");
            for (Map.Entry<String, String> entry : selectedAnswers.entrySet()) {
                System.out.println("Câu hỏi: " + entry.getKey() + " - ID câu trả lời: " + entry.getValue());
            }

            // Lấy Exam từ ExamRepository
            Exam exam = examRepository.findByStudentProfile_StudentIDAndTest_TestID(studentId,
                    Long.parseLong(testId));

            // Lưu tổng điểm và các câu trả lời
            double totalScore = 0;
            for (Map.Entry<String, String> entry : selectedAnswers.entrySet()) {
                // Tạo đối tượng StudentAnswer mới cho mỗi câu trả lời

                StudentAnswer studentAnswer = new StudentAnswer();
                studentAnswer.setStudentProfile(account.getStudentProfile());
                studentAnswer.setAnswerExamStudent(entry.getValue());
                studentAnswer.setExam(exam);
                studentAnswer.setQuestionTest(
                        questionTestRepository.findById(Long.parseLong(entry.getKey())).orElse(null));

                studentAnswer.setScore(1.0 / questionNumber);
                studentAnswer.setResultStatus(
                        answerTestService.getAnswerTestTrueByAnswerTestID(Long.parseLong(entry.getValue())));
                studentAnswerRepository.save(studentAnswer);

                totalScore = calculateScore(selectedAnswers, questionNumber);
            }
            // Cập nhật điểm sau khi kiểm tra
            // boolean isUpdated = examService.updateExamScore(exam.getExamID(), (long)
            // totalScore);
            // Cập nhật model với điểm số và tên kỳ thi
            model.addAttribute("examName", examName);
            model.addAttribute("score", totalScore);

            // Trả về view với điểm số
            return "s_score";
        } catch (Exception e) {
            e.printStackTrace();
            return "errorPage";
        }
    }

    private double calculateScore(Map<String, String> selectedAnswers, int questionNumber) {
        int correctAnswersCount = 0;

        // Duyệt qua các câu trả lời đã chọn và kiểm tra xem có đúng không
        for (String answerTestID : selectedAnswers.values()) {
            if (isCorrectAnswer(answerTestID)) {
                correctAnswersCount++;
            }
        }

        // Tính điểm
        double score = (double) correctAnswersCount / questionNumber * 10;
        return Math.round(score * 100.0) / 100.0;
    }

    private boolean isCorrectAnswer(String answerTestID) {
        try {
            // Kiểm tra đáp án đúng từ AnswerTestService
            boolean isAnswerCorrect = answerTestService.getAnswerTestTrueByAnswerTestID(Long.parseLong(answerTestID));
            return isAnswerCorrect;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Nếu có lỗi xảy ra, giả sử đáp án sai
        }
    }
}
