package com.example.project_ojt202.controllers;

import com.example.project_ojt202.models.*;
import com.example.project_ojt202.repositories.*;
import com.example.project_ojt202.services.*;
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
    private final ScoreTranscriptService scoreTranscriptService;
    private final TestService testService;
    private final LearnService learnService;
    private final AvarageScoreRepository avarageScoreRepository;

    // Constructor để inject các service và repository
    public SubmitTestController(AnswerTestService answerTestService, ExamRepository examRepository,
            TestService testService, LearnService learnService,
            AvarageScoreRepository avarageScoreRepository,
            StudentAnswerRepository studentAnswerRepository,
            QuestionTestRepository questionTestRepository,
            ScoreTranscriptService scoreTranscriptService) {
        this.examRepository = examRepository;
        this.answerTestService = answerTestService;
        this.studentAnswerRepository = studentAnswerRepository;
        this.questionTestRepository = questionTestRepository;
        this.scoreTranscriptService = scoreTranscriptService;
        this.testService = testService;
        this.learnService = learnService;
        this.avarageScoreRepository = avarageScoreRepository;
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

        // Kiểm tra studentId từ session
        String studentId = (account != null && account.getStudentProfile() != null)
                ? account.getStudentProfile().getStudentID()
                : null;

        if (studentId == null) {
            return "errorPage"; // Không có studentId, trả về trang lỗi
        }

        try {
            // Chuyển đổi selectedAnswers từ JSON thành Map
            Map<String, String> selectedAnswers = objectMapper.readValue(selectedAnswersJson, Map.class);

            // Lấy Exam từ ExamRepository
            Exam exam = examRepository.findByStudentProfile_StudentIDAndTest_TestID(studentId,
                    Long.parseLong(testId));

            if (exam == null) {
                return "errorPage"; // Không tìm thấy bài thi
            }

            // Lưu tổng điểm và các câu trả lời
            double totalScore = calculateScore(selectedAnswers, questionNumber);

            // Lưu các câu trả lời vào StudentAnswerRepository
            for (Map.Entry<String, String> entry : selectedAnswers.entrySet()) {
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
            }

            // Cập nhật điểm trong Exam
            examRepository.updateExamScore(exam.getExamID(), (long) totalScore);

            // Lấy Learn object
            UniClass uniClass = testService.getUniClassByTestId(Long.parseLong(testId));
            Learn learn = learnService.getLearnByUniClassAndStudent(uniClass.getUniClassId(), studentId);

            if (learn == null) {
                return "errorPage"; // Không tìm thấy Learn
            }

            // Lấy thông tin bảng điểm (ScoreTranscript)
            Long testID = examRepository.findTestIdByExamID(Long.parseLong(examID));
            Long scoreTranscriptID = testService.getScoreTranscriptIDByTestID(testID);
            ScoreTranscript scoreTranscript = scoreTranscriptService.findById(scoreTranscriptID);

            if (scoreTranscript == null) {
                return "errorPage"; // Không tìm thấy ScoreTranscript
            }

            int numberCol = scoreTranscript.getNumberColumn();
            int percent = scoreTranscript.getTotalPercent();
            double avgScore = totalScore / numberCol * (percent / 100.0);

            // Kiểm tra hoặc tạo mới AvarageScore
            AvarageScore avarageScore = avarageScoreRepository.findByLearn_LearnID(learn.getLearnID());
            if (avarageScore != null) {
                avarageScore.setAvarageScore(avarageScore.getAvarageScore() + avgScore);
            } else {
                avarageScore = new AvarageScore();
                avarageScore.setLearn(learn);
                avarageScore.setAvarageScore(avgScore);
            }
            avarageScoreRepository.save(avarageScore);
            System.out.println(avarageScore);

            // Cập nhật model với điểm số và tên kỳ thi
            model.addAttribute("examName", examName);
            model.addAttribute("score", totalScore);

            return "s_score"; // Trả về view điểm
        } catch (Exception e) {
            e.printStackTrace();
            return "errorPage"; // Trả về trang lỗi nếu xảy ra lỗi
        }
    }

    private double calculateScore(Map<String, String> selectedAnswers, int questionNumber) {
        int correctAnswersCount = 0;
        for (String answerTestID : selectedAnswers.values()) {
            if (isCorrectAnswer(answerTestID)) {
                correctAnswersCount++;
            }
        }
        double score = (double) correctAnswersCount / questionNumber * 10;
        return Math.round(score * 100.0) / 100.0;
    }

    private boolean isCorrectAnswer(String answerTestID) {
        try {
            return answerTestService.getAnswerTestTrueByAnswerTestID(Long.parseLong(answerTestID));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
