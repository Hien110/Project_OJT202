package com.example.project_ojt202.controllers;

import com.example.project_ojt202.services.AnswerTestService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    // Constructor để inject AnswerTestService
    public SubmitTestController(AnswerTestService answerTestService) {
        this.answerTestService = answerTestService;
    }

    @PostMapping("calculatScore")
    public String submitTest(@RequestParam("selectedAnswers") String selectedAnswersJson,
            @RequestParam("questionNumber") int questionNumber,
            @RequestParam("examName") String examName,
            Model model) {
        try {
            // Chuyển đổi selectedAnswers từ JSON thành Map
            Map<String, String> selectedAnswers = objectMapper.readValue(selectedAnswersJson, Map.class);

            // In ra các câu trả lời đã chọn
            System.out.println("Các câu trả lời đã chọn:");
            for (Map.Entry<String, String> entry : selectedAnswers.entrySet()) {
                System.out.println("Câu hỏi: " + entry.getKey() + " - ID câu trả lời: " + entry.getValue());
            }

            double score = calculateScore(selectedAnswers, questionNumber);

            model.addAttribute("examName", examName);
            model.addAttribute("score", score);

            // Trả về view với điểm số
            return "s_score";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
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
