package com.example.project_ojt202.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.ChatMessage;
import com.example.project_ojt202.services.AccountService;
import com.example.project_ojt202.services.ChatMessageService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/messages")
public class chatMessagerController {


 @Autowired
    private ChatMessageService messageService;

    @Autowired
    private AccountService accountService;
     @PostMapping("/send/{receiverAccountID}")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage request, HttpSession session,@PathVariable String receiverAccountID) {
        Account sender = (Account) session.getAttribute("account");
        Account receiver = accountService.getAccountByID(receiverAccountID); ;
        ChatMessage message = messageService.sendMessage(sender, receiver, request.getMessageContent());
        return ResponseEntity.ok(message);
    }

    @GetMapping("/history/{receiverAccountID}")
    public ResponseEntity<List<ChatMessage>> getMessageHistory(HttpSession session, @PathVariable String receiverAccountID) {
        Account sender = (Account) session.getAttribute("account");
        Account receiver = accountService.getAccountByID(receiverAccountID); // Chọn người nhận cụ thể
        List<ChatMessage> messages = messageService.getMessagesBetween(sender, receiver);
        List<ChatMessage> messages1 = messageService.getMessagesBetween(receiver, sender);

        // Kết hợp hai danh sách
        List<ChatMessage> combinedMessages = new ArrayList<>();
        combinedMessages.addAll(messages);
        combinedMessages.addAll(messages1);

        // Sắp xếp danh sách kết hợp theo thời gian
        combinedMessages.sort(Comparator.comparing(ChatMessage::getTimeStamp));

        // Trả về danh sách đã kết hợp và sắp xếp
        return ResponseEntity.ok(combinedMessages);

    }
}
