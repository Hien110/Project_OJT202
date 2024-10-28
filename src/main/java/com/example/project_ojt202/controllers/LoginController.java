package com.example.project_ojt202.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.services.AccountService;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;

@Controller
public class LoginController {
    private final AccountService accountService;

    public LoginController (AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String showLoginPage() {
        return "login"; 
    }
    @GetMapping("/homeStudent")
    public String showHomeStudentPage() {
        return "homeStudent"; 
    }
    
    @GetMapping("/homeLecture")
    public String showHomeLecturePage() {
        return "homeLecture"; 
    }

    @GetMapping("/taiLieu")
    public String showTaiLieuPage() {
        return "taiLieu"; 
    }

    
    @GetMapping("/classRoom")
    public String showClassRoom() {
        return "classRoom"; 
    }

    @GetMapping("/test1")
    public String showTest1() {
        return "test";}
    @GetMapping("/homeParent")
    public String showHomeParentPage() {
        return "homeParent"; 
    }
    @GetMapping("/test")
    public String showHomeTestPage() {
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
