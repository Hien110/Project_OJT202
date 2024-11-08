package com.example.project_ojt202.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Scheduce;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.services.SubjectService;
import com.example.project_ojt202.services.UniClassService;

@Controller
public class UniClassManagerController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UniClassService uniClassService;

    @Autowired 
    private LectureProfileService lectureProfileService;

    @GetMapping("/uniClassMajor")
    public String showUniClassMajorPage() {
        return "a_uniClassMajor"; 
    }

     @PostMapping("/uniClassMajor")
    public String handleMajorSelection(@RequestParam String major, Model model) {
        model.addAttribute("selectedMajor", major);
        return "a_uniClassEachMajor";  
    }

    @GetMapping("/uniClassManager/{major}/{specialization}")
    public String showUniClassEachMajor(@PathVariable String major, @PathVariable int specialization, Model model) {
        List<Subject> subjects = subjectService.getSubjectByMajorIDAndTernNo(major, specialization);
        Map<Subject, List<UniClass>> subjectClassMap = new HashMap<>();
        Map<String, Integer> countStudentEachUniClass = new HashMap<>(); // Map để lưu số lượng lớp theo subjectID

        for (Subject subject : subjects) {  
        List<UniClass> uniClasses = uniClassService.getUniClassBySubjectID(subject.getSubjectID());
        Subject key = new Subject(subject.getSubjectID(), subject.getSubjectName());
        subjectClassMap.put(key, uniClasses);

        for (UniClass uniClass : uniClasses) {
            List<StudentProfile> scheduces = lectureProfileService.getLecProfileByMajorID(uniClass.getUniClassId());
            classSlotMap.put(uniClass.getUniClassId(), scheduces);
        }
    }
    }
    
}
