package com.example.project_ojt202.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.Notification;
import com.example.project_ojt202.models.ParentProfile;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.services.AccountService;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.services.NotificationService;
import com.example.project_ojt202.services.ParentProfileService;
import com.example.project_ojt202.services.StudentProfileService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;

@Controller
public class LoginController {
    private final AccountService accountService;
    private final NotificationService notificationService;
    private final StudentProfileService studentProfileService;
    private final LectureProfileService lectureProfileService;
    private final ParentProfileService parentProfileService;
    
    public LoginController(AccountService accountService, NotificationService notificationService,
            StudentProfileService studentProfileService, LectureProfileService lectureProfileService,
            ParentProfileService parentProfileService) {
        this.accountService = accountService;
        this.notificationService = notificationService;
        this.studentProfileService = studentProfileService;
        this.lectureProfileService = lectureProfileService;
        this.parentProfileService = parentProfileService;
    }

    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/login")
    public String showLoginPage1() {
        return "login";
    }

    @GetMapping("/home")
    public String showHomeStudentPage(Model model, HttpSession session) {
        List<Notification> notifications = notificationService.findAll();
        model.addAttribute("notifications", notifications);
        return "home";
    }

    @GetMapping("/taiLieu")
    public String showTaiLieuPage() {
        return "taiLieu";
    }

    @GetMapping("/feedBack")
    public String showFeedBackPage() {
        return "afeedBack";
    }
    
    @GetMapping("/viewfeedBack")
    public String showViewFeedBackPage() {
        return "viewfeedback";
    }
    
    @GetMapping("/classRoom")
    public String showClassRoom() {
        return "classRoom";
    }
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/login")
    public String login(@RequestParam("accountID") String accountID,
            @RequestParam("accountPassword") String accountPassword,
            Model model,
            HttpSession session) {
            List<Notification> notifications = (List<Notification>) session.getAttribute("notifications");
        if (notifications == null) {
            notifications = notificationService.findAll();
            session.setAttribute("notifications", notifications);
        }
        Account account = accountService.getAccountByAccountIDAndPassword(accountID, accountPassword);
        if (account != null) {
            session.setAttribute("account", account);
            try {
                switch (account.getAccountRole()) {
                    case "student":
                        StudentProfile studentProfile = account.getStudentProfile();
                        if (studentProfile == null || studentProfile.getStudentID() == null) {
                            throw new Exception("Student profile or StudentID is missing.");
                        }
                        StudentProfile student = studentProfileService
                                .getStudentProfileByStudentID(studentProfile.getStudentID());
                        session.setAttribute("profileAccount", student);
                        
                        break;
                    case "lecture": 
                        LectureProfile lectureProfile = account.getLectureProfile();
                        if (lectureProfile == null || lectureProfile.getLectureID() == null) {
                            throw new Exception("Lecture profile or StudentID is missing.");
                        }
                        LectureProfile lecture = lectureProfileService.getLecProfileByLectureID(lectureProfile.getLectureID());
                        session.setAttribute("profileAccount", lecture);
                        break;
                    case "parent": 
                        ParentProfile parentProfile = account.getParentProfile();
                        if (parentProfile == null || parentProfile.getParentID() == null) {
                            throw new Exception("Lecture profile or StudentID is missing.");
                        }
                        ParentProfile parent = parentProfileService.getParentProfileByParentID(parentProfile.getParentID());
                        session.setAttribute("profileAccount", parent);
                        break;
                    case "admin":
                    session.setAttribute("profileAccount", "Admin");
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                return "redirect:/";
            }
            return "redirect:/home";

        } else {
            model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng");
            return "login";
        }
    }
}
