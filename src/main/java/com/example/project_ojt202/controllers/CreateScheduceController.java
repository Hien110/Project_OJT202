package com.example.project_ojt202.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Scheduce;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.ScheduceService;
import com.example.project_ojt202.services.SubjectService;
import com.example.project_ojt202.services.UniClassService;

import org.springframework.ui.Model;


@Controller

public class CreateScheduceController {
    private final SubjectService subjectService;
    private final UniClassService uniClassService;
    private final ScheduceService scheduceService;
    public CreateScheduceController (SubjectService subjectService, UniClassService uniClassService, ScheduceService scheduceService){
        this.subjectService = subjectService;
        this.uniClassService = uniClassService;
        this.scheduceService = scheduceService;
    }
    @GetMapping("/scheduceMajor")
    public String showScheduceMajorPage() {
        return "a_scheduceMajor"; 
    }

     @PostMapping("/scheduceMajor")
    public String handleMajorSelection(@RequestParam("major") String major, Model model) {
        model.addAttribute("selectedMajor", major);

        return "a_scheduceEachMajor";  
    }
    @GetMapping("/scheduceOfSubject")
    public String showScheduceSubjectPage() {
        return "a_scheduceSubject"; 
    }

    @PostMapping("/scheduceOfSubject")
public String handleSpecialization(@RequestParam("major") String major, 
                                   @RequestParam("specialization") int specialization, 
                                   Model model) {
    List<Subject> subjects = subjectService.getSubjectByMajorIDAndTernNo(major, specialization);
    model.addAttribute("subjects", subjects);

    Map<Subject, List<UniClass>> subjectClassMap = new HashMap<>();
    Map<Long, List<Scheduce>> classSlotMap = new HashMap<>(); // Map để lưu slot theo uniClassID

    for (Subject subject : subjects) {
        List<UniClass> uniClasses = uniClassService.getUniClassBySubjectID(subject.getSubjectID());
        Subject key = new Subject(subject.getSubjectID(), subject.getSubjectName());
        subjectClassMap.put(key, uniClasses);

        // Lấy slot của từng uniClass và thêm vào classSlotMap
        for (UniClass uniClass : uniClasses) {
            List<Scheduce> scheduces = scheduceService.findScheduceOfUniClass(uniClass.getUniClassId());
            classSlotMap.put(uniClass.getUniClassId(), scheduces);
        }
    }

    model.addAttribute("subjectClassMap", subjectClassMap);
    model.addAttribute("classSlotMap", classSlotMap); // Thêm Map slot vào Model
    return "a_scheduceSubject";
}

    
}