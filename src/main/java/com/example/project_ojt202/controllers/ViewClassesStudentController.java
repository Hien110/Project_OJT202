package com.example.project_ojt202.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.models.ScoreTranscript;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.LearnService;
import com.example.project_ojt202.services.ScoreTranscriptService;
import com.example.project_ojt202.services.UniClassService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Student")
public class ViewClassesStudentController {
    private final LearnService learnService;
    private final UniClassService uniClassService;
    private final ScoreTranscriptService scoreTranscriptService;

    // Constructor injection of LearnService
    public ViewClassesStudentController(LearnService learnService, UniClassService uniClassService,
            ScoreTranscriptService scoreTranscriptService) {
        this.learnService = learnService;
        this.uniClassService = uniClassService;
        this.scoreTranscriptService = scoreTranscriptService;
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

        // Thêm danh sách lớp vào model để hiển thị trên view
        model.addAttribute("classes", classes);

        // Trả về view list-classforstudent để hiển thị danh sách lớp
        return "s_list-classforstudent";
    }

    @GetMapping("/list-classforstudent/{uniClassId}/Learning")
    public String getTestsByUniClassId(@PathVariable String uniClassId, Model model) {
    
        if (uniClassId != null) {
            // Lấy danh sách ScoreTranscript thông qua subjectId
            List<ScoreTranscript> scoreTranscripts = scoreTranscriptService.getScoreTranscriptsBySubjectId(uniClassId);
            model.addAttribute("scoreTranscripts", scoreTranscripts);
        }    
    
        // Trả về view để hiển thị
        return "s_subjectLearning";
    }
}
