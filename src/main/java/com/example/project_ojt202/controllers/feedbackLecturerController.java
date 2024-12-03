package com.example.project_ojt202.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project_ojt202.models.StudentFeedback;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.services.StudentFeedbackService;


@Controller
@RequestMapping("/lecturer")
public class feedbackLecturerController {

    private final LectureProfileService leturerService;
    private final StudentFeedbackService feedbackService;

    public feedbackLecturerController(LectureProfileService leturerService, StudentFeedbackService feedbackService) {
        this.leturerService = leturerService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/{lectureID}/l_viewClassLecturer")
    public String getClasses(@PathVariable("lectureID") String lectureID, Model model) {

        List<UniClass> classes = leturerService.getClassesForLecturer(lectureID);
        model.addAttribute("classes", classes);
        return "l_viewClassLecturer";
    }

    @GetMapping("/l_viewClassLecturer/{uniClassId}/l_feedbackStudent")
    public String listFeedbacks(@PathVariable("uniClassId") Long uniClassId, Model model) {
        List<StudentFeedback> feedbacks = feedbackService.getFeedbackByUniClassId(uniClassId);
        model.addAttribute("feedbacks", feedbacks);
        return "l_feedbackStudent";
    }
}
