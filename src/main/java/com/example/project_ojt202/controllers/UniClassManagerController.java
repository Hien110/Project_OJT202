package com.example.project_ojt202.controllers;

import java.util.Arrays;
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

import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.LearnService;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.services.SubjectService;
import com.example.project_ojt202.services.UniClassService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class UniClassManagerController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UniClassService uniClassService;

    @Autowired
    private LearnService learnService;

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
        List<LectureProfile> lectures = lectureProfileService.getLecProfileByMajorID(major);

        Map<Subject, List<UniClass>> subjectClassMap = new HashMap<>();
        Map<String, Integer> countStudentEachUniClass = new HashMap<>(); // Map để lưu số lượng lớp theo subjectID

        List<String> semesterList = Arrays.asList("Fall23", "Spring24", "Summer24", "Fall24", "Spring25", "Summer25");
        for (Subject subject : subjects) {
            List<UniClass> uniClasses = uniClassService.getUniClassBySubjectID(subject.getSubjectID());
            Subject key = new Subject(subject.getSubjectID(), subject.getSubjectName());
            subjectClassMap.put(key, uniClasses);

            for (UniClass uniClass : uniClasses) {
                List<Learn> learn = learnService.getLearnByUniClass(uniClass.getUniClassId());
                countStudentEachUniClass.put(uniClass.getUniClassName(), learn.size());
            }
        }
        model.addAttribute("Subjects", subjects);
        model.addAttribute("subjectClassMap", subjectClassMap);
        model.addAttribute("countStudentEachUniClass", countStudentEachUniClass);
        model.addAttribute("semesterList", semesterList);
        model.addAttribute("lectures", lectures);
        model.addAttribute("major", major);
        model.addAttribute("specialization", specialization);
        return "a_managerUniClass";
    }

    @PostMapping("/editUniClass/{major}/{specialization}")
    public String editUniClass(@PathVariable String major, @PathVariable int specialization,
    @RequestParam("mapDataInputLecture") String mapDataInputLectureJson, Model model) {
       try {
        ObjectMapper mapper1 = new ObjectMapper();
        Map<String, String> mapDataInputLecture = mapper1.readValue(mapDataInputLectureJson, new TypeReference<Map<String, String>>() {});

        mapDataInputLecture.forEach((key, value) -> {
            Long uniClassId = Long.parseLong(key);

            LectureProfile lectureProfile = lectureProfileService.getLecProfileByLectureID(value);
            UniClass uniClass = uniClassService.getUniClassById(uniClassId);
            uniClass.setLectureProfile(lectureProfile);
            uniClassService.saveUniClass(uniClass);
        });

    } catch (Exception e) {
        e.printStackTrace();
    }

    return  "redirect:/uniClassManager/" + major + "/" + specialization;
    }


    @GetMapping("/createUniClass/{major}/{specialization}")
    public String showPageCreateUniClass(@PathVariable String major, @PathVariable int specialization,@RequestParam("semester") String semester, Model model) {
        List<Subject> subjects = subjectService.getSubjectByMajorIDAndTernNo(major, specialization);
        List<LectureProfile> lectures = lectureProfileService.getLecProfileByMajorID(major);

        model.addAttribute("Subjects", subjects);
        model.addAttribute("major", major);
        model.addAttribute("semester", semester);
        model.addAttribute("specialization", specialization);
        model.addAttribute("lectures", lectures);
        return "a_createUniClass";
    }
    
    @PostMapping("/createUniClass/{major}/{specialization}")
        public String createUniClass(@PathVariable String major, @PathVariable int specialization,
        @RequestParam("mapDataUniClass") String mapDataUniClassJson, Model model){
            try {
                ObjectMapper mapper1 = new ObjectMapper();
                Map<String, String> mapDataUniClass = mapper1.readValue(mapDataUniClassJson, new TypeReference<Map<String, String>>() {});
        
                mapDataUniClass.forEach((key, value) -> {
                    LectureProfile lectureProfile = lectureProfileService.getLecProfileByLectureID(value.split("_")[0]);
                    Subject subject = subjectService.getSubjectBySubjectID(value.split("_")[1]);
                    UniClass uniClass = new UniClass();
                    uniClass.setUniClassName(key);
                    uniClass.setNumberStudent(value.split("_")[2]);
                    uniClass.setLectureProfile(lectureProfile);
                    uniClass.setSemester(value.split("_")[3]);
                    uniClass.setSubject(subject);
                    uniClassService.saveUniClass(uniClass);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "redirect:/uniClassManager/" + major + "/" + specialization;
    }
    
}
