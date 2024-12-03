package com.example.project_ojt202.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Exam;
import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.repositories.ExamRepository;
import com.example.project_ojt202.services.LearnService;

@Controller
public class ViewScoreOfClassController {

    @Autowired
    private LearnService learnService;

    @Autowired
    private ExamRepository examRepository;

    
    @GetMapping("/viewScoreOfClass")
    public String viewScore(
            @RequestParam("uniClassId") Long uniClassId,
            Model model) {

        List<Learn> learns = learnService.getLearnByUniClass(uniClassId);
        List<Exam> exams = examRepository.findAll();

        model.addAttribute("uniClassId", uniClassId);
        model.addAttribute("learns", learns);
        model.addAttribute("exams", exams);

        return "viewScoreOfClass";
    }
}
