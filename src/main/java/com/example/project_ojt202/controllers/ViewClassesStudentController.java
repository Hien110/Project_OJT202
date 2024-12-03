package com.example.project_ojt202.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.models.Test;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.LearnService;
import com.example.project_ojt202.services.ScoreTranscriptService;
import com.example.project_ojt202.services.TestService;
import com.example.project_ojt202.services.UniClassService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Student")
public class ViewClassesStudentController {
    private final LearnService learnService;
    private final ScoreTranscriptService scoreTranscriptService;
    private final TestService testService;

    // Constructor injection of LearnService
    public ViewClassesStudentController(LearnService learnService, UniClassService uniClassService,
            ScoreTranscriptService scoreTranscriptService, TestService testService) {
        this.learnService = learnService;
        this.scoreTranscriptService = scoreTranscriptService;
        this.testService = testService;
    }

    @GetMapping("/list-classforstudent")
    public String getClasses(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        String studentID = null;

        // Kiểm tra xem account có tồn tại và có thông tin studentProfile không
        if (account != null && account.getStudentProfile() != null) {
            studentID = account.getStudentProfile().getStudentID(); // Lấy studentID từ session
        }

        // Lấy danh sách lớp học của sinh viên
        List<Learn> classes = learnService.getLearnByStudentID(studentID);

        // Thêm thông tin về khả năng hiển thị nút phản hồi
        Map<Long, Boolean> feedbackAvailabilityMap = classes.stream()
                .map(Learn::getUniClass)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        UniClass::getUniClassId,
                        uniClass -> {
                            if (uniClass.getDateEndLearn() == null) {
                                return false;
                            }
                            LocalDate now = LocalDate.now();
                            LocalDate startFeedbackDate = uniClass.getDateEndLearn().minusDays(14);
                            return (now.isAfter(startFeedbackDate) || now.isEqual(startFeedbackDate)) &&
                                    (now.isBefore(uniClass.getDateEndLearn())
                                            || now.isEqual(uniClass.getDateEndLearn()));
                        }));

        // Gắn dữ liệu vào model
        model.addAttribute("classes", classes);
        model.addAttribute("feedbackAvailabilityMap", feedbackAvailabilityMap);
        return "s_list-classforstudent"; // Tên file HTML view
    }

    @GetMapping("/list-classforstudent/{SubId}/{Uniclass}/Learning")
    public String getTestsByUniClassId(@PathVariable String SubId, @PathVariable String Uniclass, Model model) {
        if (SubId != null) {
            // Lấy danh sách bài kiểm tra theo uniClassId
            List<Test> tests = testService.getAllTestByUniClass(Long.parseLong(Uniclass));
            model.addAttribute("test", tests);
        }
        return "s_subjectLearning";
    }
}
