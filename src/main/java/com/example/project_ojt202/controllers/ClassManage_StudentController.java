package com.example.project_ojt202.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClassManage_StudentController {

    @GetMapping("/classManage_Student")
    public String index(HttpSession session, Model model) {
        
        // Trả về view "classManage_Student"
        return "classManage_Student"; 
    }

    
}
