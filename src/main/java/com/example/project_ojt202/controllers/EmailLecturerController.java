package com.example.project_ojt202.controllers;

import com.example.project_ojt202.services.EmailLecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmailLecturerController {

    @Autowired
    private EmailLecturerService emailLecturerService;

    @PostMapping("/sendEmailsLecturer")
    public String sendEmails() {
        emailLecturerService.sendEmails();
        return "redirect:/home";
    }
}
