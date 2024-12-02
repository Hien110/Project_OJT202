package com.example.project_ojt202.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.ChatMessage;
import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.services.AccountService;
import com.example.project_ojt202.services.ChatMessageService;
import com.example.project_ojt202.services.LearnService;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.services.StudentProfileService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatMessageSendController {

    @Autowired
    private ChatMessageService messageService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private LearnService learnService;

    @Autowired
    private LectureProfileService lectureProfileService;

    @Autowired
    private StudentProfileService studentProfileService;
    @GetMapping("/chatMessage/{accountID}")
    public String sendMessage(Model model, HttpSession session, @PathVariable("accountID") String accountReceiverID) {
        Account account1 = (Account) session.getAttribute("account");
        Account accountReceiver = accountService.getAccountByID(accountReceiverID);
        if(accountReceiver.getAccountRole().equals("lecturer")){
            LectureProfile lectureProfile = lectureProfileService.findByLectureID(accountReceiverID);
            model.addAttribute("lectureProfile", lectureProfile);
        } else if (accountReceiver.getAccountRole().equals("student")){
            StudentProfile studentProfile = studentProfileService.getStudentProfileByStudentID(accountReceiverID);
            model.addAttribute("studentProfile", studentProfile);
        } else {
            model.addAttribute("Admin", "Admin");
        }
        List<Account> accounts = accountService.getAllAccount();
        Map<Account, LectureProfile> listMessageLecture = new HashMap<Account, LectureProfile>();
        for(Account account : accounts) {
            if (account.getAccountRole().equals("lecturer")) {
                listMessageLecture.put(account, account.getLectureProfile());
            } 
        }

        if (account1.getAccountRole().equals("student")) {
            List<Learn> learns = learnService.getLearnByStudentID(account1.getAccountID());
            for(Learn learn : learns) { 
                System.out.println(learn.getUniClass().getLectureProfile().getFirstName() + " " + learn.getUniClass().getLectureProfile().getLastName());
            }
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
        model.addAttribute("accountReceiverID", accountReceiverID);
        model.addAttribute("accountReceiver", accountReceiver);
        return "chatMessage.html";
    }
}    
