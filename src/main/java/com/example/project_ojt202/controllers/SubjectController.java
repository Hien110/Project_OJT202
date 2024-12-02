package com.example.project_ojt202.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.models.Major;
import com.example.project_ojt202.models.PrerequisiteSubject;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.repositories.PrerequisiteSubjectRepository;
import com.example.project_ojt202.services.LearnService;
import com.example.project_ojt202.services.MajorService;
import com.example.project_ojt202.services.SubjectService;
import com.example.project_ojt202.services.UniClassService;

@Controller
public class SubjectController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private MajorService majorService;
    @Autowired
    private UniClassService uniClassService;
    @Autowired
    private LearnService learnService;
    @Autowired
    private PrerequisiteSubjectRepository prerequisiteSubjectRepository;

    @GetMapping("/allOfSubject")
    public String listStudents(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, Model model) {
        Page<Subject> subjectPage = subjectService.getAllSubjects(page, size);
        List<Major> majors = majorService.findAllMajor();
        List<PrerequisiteSubject> prerequisiteSubjects = prerequisiteSubjectRepository.findAll();

        model.addAttribute("prerequisiteSubjects", prerequisiteSubjects);
        model.addAttribute("majorIds", majors);
        model.addAttribute("subjects", subjectPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", subjectPage.getTotalPages());
        return "allOfSubject"; // Trang sẽ render
    }

    @GetMapping("/classesOfSpecialization")
    public String getSubjectsByMajorAndTern(
            @RequestParam("specialization") int specialization,
            @RequestParam("majorID") String majorID,
            @RequestParam("studentID") String studentID,
            Model model) {
        List<Subject> subjects = subjectService.getSubjectsByMajorAndTern(majorID, specialization);
        List<UniClass> uniClasses = uniClassService.getUniClassesBySubjects(subjects);
        List<Learn> learns = learnService.getLearnByStudentID(studentID);
        Learn maxTernNoLearn = learnService.findLearnWithMaxTernNo(learns);

        int maxTernNo = 0;
        if(maxTernNoLearn != null) {
            maxTernNo = maxTernNoLearn.getUniClass().getSubject().getTernNo();
        }

        model.addAttribute("maxTernNo", maxTernNo);
        model.addAttribute("learns", learns);
        model.addAttribute("uniClasses", uniClasses);
        return "classesOfSpecialization";
    }
}
