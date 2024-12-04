package com.example.project_ojt202.controllers;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.StudentFeedback;
import com.example.project_ojt202.services.StudentFeedbackService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class StudentFeedbackController {
    private final StudentFeedbackService studentFeedbackService;

    // Constructor
    public StudentFeedbackController(StudentFeedbackService studentFeedbackService) {
        this.studentFeedbackService = studentFeedbackService;
    }

    @GetMapping("/feedback/class/{classId}")
    public String getFeedbackByClass(@PathVariable Long classId, Model model, HttpSession session) {
        // Lấy danh sách feedback từ service
        List<StudentFeedback> feedbackList = studentFeedbackService.getFeedbackByClassId(classId);

        // Xử lý dữ liệu để chuẩn bị hiển thị theo nhóm học sinh
        Map<String, Map<String, Object>> studentFeedbackMap = new LinkedHashMap<>();

        // Nhóm feedback theo học sinh
        for (StudentFeedback feedback : feedbackList) {
            String studentId = feedback.getStudentProfile().getStudentID(); // ID học sinh là String
            Account account = (Account) session.getAttribute("account");
            String studentFullName = "Ẩn danh";
            if (account != null && account.getAccountRole().equals("admin")) {
            studentFullName = feedback.getStudentProfile().getFirstName() + " " + feedback.getStudentProfile().getLastName(); // Lấy tên học sinh
            }
            // Tạo hoặc lấy bản đồ cho học sinh nếu chưa có
            Map<String, Object> studentFeedback = studentFeedbackMap.computeIfAbsent(studentId, k -> new LinkedHashMap<>());

            // Lấy tất cả các phản hồi cho học sinh này và phân loại theo câu hỏi
            List<StudentFeedback> studentFeedbackList = feedbackList.stream()
                .filter(f -> f.getStudentProfile().getStudentID().equals(studentId))
                .collect(Collectors.toList());

            // Lặp qua các phản hồi và gán các giá trị score vào các câu hỏi
            for (int i = 0; i < studentFeedbackList.size(); i++) {
                StudentFeedback feedbackForQuestion = studentFeedbackList.get(i);
                int score = feedbackForQuestion.getFeedbackChoice().getScore();
                studentFeedback.put("Q" + (i + 1), score); // Gán giá trị score vào các cột Q1, Q2, ...
            }

            // Tính điểm GPA (trung bình của các câu hỏi)
            double gpa = calculateGPA(studentFeedbackList);
            studentFeedback.put("GPA", gpa); // Thêm GPA vào bản đồ học sinh

            // Thêm thông tin bổ sung cho học sinh
            studentFeedback.put("HIT", studentFullName); // Hiển thị tên học sinh thay vì ID
            studentFeedback.put("COMMENT", feedback.getFeedbackText()); // Thêm comment của học sinh
        }

        // Chuyển map thành list để truyền cho giao diện
        List<Map<String, Object>> feedbackTable = new ArrayList<>(studentFeedbackMap.values());

        // Gửi dữ liệu sang giao diện
        model.addAttribute("feedbackTable", feedbackTable);

        return "a_listFeedBack";
    }

    // Hàm tính GPA (trung bình điểm)
    private double calculateGPA(List<StudentFeedback> feedbackList) {
        double sum = 0;
        int count = 0;
        for (StudentFeedback feedback : feedbackList) {
            sum += feedback.getFeedbackChoice().getScore(); // Cộng tất cả điểm của các câu hỏi
            count++;
        }
        return count > 0 ? sum / count : 0; // Tính trung bình điểm
    }
}
