package com.example.project_ojt202.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.Scheduce;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.services.ScheduceService;
import com.example.project_ojt202.services.SubjectService;
import com.example.project_ojt202.services.UniClassService;

import org.springframework.ui.Model;


@Controller

public class CreateScheduceController {
    private final SubjectService subjectService;
    private final UniClassService uniClassService;
    private final ScheduceService scheduceService;
    private final LectureProfileService lectureProfileService;
    public CreateScheduceController (SubjectService subjectService, UniClassService uniClassService, ScheduceService scheduceService, LectureProfileService lectureProfileService){
        this.subjectService = subjectService;
        this.uniClassService = uniClassService;
        this.scheduceService = scheduceService;
        this.lectureProfileService = lectureProfileService;
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
    List<LectureProfile> lectures = lectureProfileService.getLecProfileByMajorID(major);
    model.addAttribute("subjects", subjects);
    model.addAttribute("lectures", lectures);
    Map<Subject, List<UniClass>> subjectClassMap = new HashMap<>();
    Map<Long, List<Scheduce>> classSlotMap = new HashMap<>(); // Map để lưu slot theo uniClassID

    Map<String, Integer> subjectClassCountMap = new HashMap<>(); // Map để lưu số lượng lớp theo subjectID

    for (Subject subject : subjects) {
        List<UniClass> uniClasses = uniClassService.getUniClassBySubjectID(subject.getSubjectID());
        Subject key = new Subject(subject.getSubjectID(), subject.getSubjectName());
        subjectClassMap.put(key, uniClasses);

        // Lưu số lượng lớp vào Map
        subjectClassCountMap.put(subject.getSubjectID(), uniClasses.size());

        // Lấy slot của từng uniClass và thêm vào classSlotMap
        for (UniClass uniClass : uniClasses) {
            List<Scheduce> scheduces = scheduceService.findScheduceOfUniClass(uniClass.getUniClassId());
            classSlotMap.put(uniClass.getUniClassId(), scheduces);
        }
    }

    
    List<Map<String, String>> slots = new ArrayList<>();
    
    Map<String, String> slot1 = new HashMap<>();
    slot1.put("name", "Slot 1");
    slot1.put("startTime", "7:30");
    slot1.put("endTime", "9:45");
    slots.add(slot1);
    
    Map<String, String> slot2 = new HashMap<>();
    slot2.put("name", "Slot 2");
    slot2.put("startTime", "10:00");
    slot2.put("endTime", "12:15");
    slots.add(slot2);

    Map<String, String> slot3 = new HashMap<>();
    slot3.put("name", "Slot 3");
    slot3.put("startTime", "12:30");
    slot3.put("endTime", "14:45");
    slots.add(slot3);

    Map<String, String> slot4 = new HashMap<>();
    slot4.put("name", "Slot 4");
    slot4.put("startTime", "15:00");
    slot4.put("endTime", "17:15");
    slots.add(slot4);

    Map<String, String> slot5 = new HashMap<>();
    slot5.put("name", "Slot 5");
    slot5.put("startTime", "17:45");
    slot5.put("endTime", "19:15");
    slots.add(slot5);

    Map<String, String> slot6 = new HashMap<>();
    slot6.put("name", "Slot 6");
    slot6.put("startTime", "19:30");
    slot6.put("endTime", " 21:00");
    slots.add(slot6);
    // Danh sách các ngày trong tuần
    List<String> days = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

    model.addAttribute("slots", slots);
    model.addAttribute("days", days);
    model.addAttribute("subjectClassMap", subjectClassMap);
    model.addAttribute("classSlotMap", classSlotMap); // Thêm Map slot vào Model
    model.addAttribute("subjectClassCountMap", subjectClassCountMap); // Thêm Map số lượng lớp vào Model

    return "a_scheduceSubject"; // Trả về view
}


    
}