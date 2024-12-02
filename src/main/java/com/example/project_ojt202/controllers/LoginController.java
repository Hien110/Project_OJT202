package com.example.project_ojt202.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.ChatMessage;
import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.Notification;
import com.example.project_ojt202.models.ParentProfile;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.services.AccountService;
import com.example.project_ojt202.services.ChatMessageService;
import com.example.project_ojt202.services.LearnService;
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
    
    
    @Autowired
        private ChatMessageService messageService;
    
    @Autowired
        private LearnService learnService;
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
    Collections.reverse(notifications);
    // Lọc thông báo hôm nay
    LocalDate today = LocalDate.now();
    List<Notification> todayNotifications = notifications.stream()
            .filter(notification -> notification.getNotificationDate().isEqual(today))
            .toList();
    
    // Lưu vào session để có thể hiển thị trên header
    session.setAttribute("todayNotifications", todayNotifications);
    model.addAttribute("notifications", notifications);
    model.addAttribute("todayNotifications", todayNotifications); // Danh sách thông báo hôm nay
        Account account1 = (Account) session.getAttribute("account");
        List<Account> accounts = accountService.getAllAccount();
        Map<Account, LectureProfile> listMessageLecture = new HashMap<Account, LectureProfile>();
        for(Account account : accounts) {
            if (account.getAccountRole().equals("lecturer")) {
                listMessageLecture.put(account, account.getLectureProfile());
            } 
        }

        if (account1.getAccountRole().equals("student")) {
            List<Learn> learns = learnService.getLearnByStudentID(account1.getAccountID());
            model.addAttribute("listMessageStudent", learns);
        }

        List<ChatMessage> messagesReceiver = messageService.getMessagesReceiver(account1);

        Iterator<ChatMessage> iterator = messagesReceiver.iterator();
        while (iterator.hasNext()) {
            ChatMessage message = iterator.next();
            if (!message.getAccountSender().getAccountRole().equals("student")) {
                iterator.remove(); // Xóa phần tử một cách an toàn
            }
        }
        
        List<ChatMessage> messagesSender = messageService.getMessagesSender(account1); 

        Iterator<ChatMessage> iterator1 = messagesSender.iterator();
        while (iterator.hasNext()) {
            ChatMessage message = iterator1.next();
            if (!message.getAccountSender().getAccountRole().equals("student")) {
                iterator1.remove(); 
            }
        }
        List<ChatMessage> combinedMessages = new ArrayList<>();
        combinedMessages.addAll(messagesReceiver); 
        combinedMessages.addAll(messagesSender); 
        combinedMessages.sort(Comparator.comparing(ChatMessage::getTimeStamp));
  
        Map<Account, String> chatStudentMap = new HashMap<Account, String>();
        for (ChatMessage message : combinedMessages) {
            if (message.getAccountSender().getAccountRole().equals("lecturer")) {
                chatStudentMap.put(message.getAccountReceiver(), " ");
            } else {
                chatStudentMap.put(message.getAccountSender(), " ");
            }
        }

        model.addAttribute("chatStudentMap", chatStudentMap);
        model.addAttribute("accounts", accounts);
        model.addAttribute("listMessageLecture", listMessageLecture);
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
                    case "lecturer": 
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
