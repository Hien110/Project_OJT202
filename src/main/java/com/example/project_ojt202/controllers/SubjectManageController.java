package com.example.project_ojt202.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SubjectManageController {

    @GetMapping("/subjectManage")
    public String index() {
        return "subjectManage"; 
    }
}