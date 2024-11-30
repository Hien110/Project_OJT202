package com.example.project_ojt202.controllers;

import com.example.project_ojt202.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/success")
    public String index2() {
        return "success"; // Render the upload page without any data initially
    }

    @PostMapping("/sendEmails")
    public String sendEmails() {
        emailService.sendEmails();
        return "redirect:/home";
    }
}
