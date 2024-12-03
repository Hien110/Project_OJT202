package com.example.project_ojt202.controllers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.UniClassService;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class ListTestController {

    private final UniClassService uniClassService;

    public ListTestController(UniClassService uniClassService) {
        this.uniClassService = uniClassService;
    }

    @GetMapping("/listClassesTest")
    public String listClasses(@RequestParam(value = "semester", required = false) String semester, HttpSession session,
            Model model) {
        Account account = (Account) session.getAttribute("account");

        String lectureID = (account != null && account.getLectureProfile() != null)
                ? account.getLectureProfile().getLectureID()
                : null;

        List<UniClass> uniClasses = null;
        if (lectureID != null) {
            uniClasses = uniClassService.getClassesByLectureID(lectureID);
        }

        // Lọc lớp theo học kỳ nếu semester được chọn
        if (semester != null && !semester.isEmpty()) {
            uniClasses = uniClasses.stream()
                    .filter(u -> u.getSemester().equals(semester))
                    .collect(Collectors.toList());
        }

        // Lấy danh sách các học kỳ duy nhất
        List<String> semesters = uniClasses.stream()
                .map(UniClass::getSemester)
                .distinct()
                .collect(Collectors.toList());

        // Gửi các giá trị vào model
        model.addAttribute("uniClasses", uniClasses);
        model.addAttribute("semesters", semesters);
        model.addAttribute("selectedSemester", semester); // Gửi học kỳ đã chọn vào view

        return "l_listClassTest";
    }
}
