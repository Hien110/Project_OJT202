package com.example.project_ojt202.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.Notification;
import com.example.project_ojt202.services.AccountService;
import com.example.project_ojt202.services.NotificationService;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;

@Controller
public class LoginController {
    private final AccountService accountService;
    private final NotificationService notificationService;
    public LoginController (AccountService accountService, NotificationService notificationService){
        this.accountService = accountService;
        this.notificationService= notificationService;
    }

    @GetMapping("/")
    public String showLoginPage() {
        return "login"; 
    }

    @GetMapping("/login")
    public String showLoginPage1() {
        return "login"; 
    }
    @GetMapping("/homeStudent")
    public String showHomeStudentPage(Model model ) {
        List<Notification> notifications = notificationService.findAll();
        model.addAttribute("notifications", notifications);
        return "homeStudent"; 
    }
    
    @GetMapping("/homeLecture")
    public String showHomeLecturePage(Model model ) {
        List<Notification> notifications = notificationService.findAll();
        model.addAttribute("notifications", notifications);
        return "homeLecture"; 
    }

    @GetMapping("/taiLieu")
    public String showTaiLieuPage() {
        return "taiLieu"; 
    }
    @GetMapping("/homeAdmin")
    public String showHomeAdminPage(Model model ) {
        List<Notification> notifications = notificationService.findAll();
        model.addAttribute("notifications", notifications);
        return "homeAdmin"; 
    }

    @GetMapping("/classRoom")
    public String showClassRoom() {
        return "classRoom"; 
    }

    @GetMapping("/homeParent")
    public String showHomeParentPage() {
        return "homeParent"; 
    }
    @GetMapping("/test")
    public String test() {
        return "test"; 
    }

    @PostMapping("/login")
    public String login(@RequestParam("accountID") String accountID, 
                        @RequestParam("accountPassword") String accountPassword, 
                        Model model) {
        
        Account account = accountService.getAccountByAccountIDAndPassword(accountID, accountPassword);
        if (account != null) {
            // Kiểm tra vai trò của tài khoản
            String accountRole = account.getAccountRole(); 
            
            switch (accountRole) {
                case "student":
                    return "redirect:/homeStudent"; 
                case "admin":
                    return "redirect:/homeAdmin"; 
                case "lecture":
                    return "redirect:/homeLecture"; 
                case "parent":
                    return "redirect:/homeParent"; 
                default:
                    model.addAttribute("error", "Role not recognized"); 
                    return "login"; 
            }
        } else {
            model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng");
            return "login"; 
        }
    }    
}
