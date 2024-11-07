package com.example.project_ojt202.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeacherFunctionController {

    @GetMapping("/l_lectureControl")
    public String showTeacherFunctions() {
        return "l_lectureControl"; 
    }
}
