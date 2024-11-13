package com.example.project_ojt202.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class test1 {
    @GetMapping("/test1")
    public String test() {
        return "homeStudent"; 
    }
}
