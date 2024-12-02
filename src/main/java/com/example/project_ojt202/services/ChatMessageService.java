package com.example.project_ojt202.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.ChatMessage;
import com.example.project_ojt202.repositories.ChatMessageRepository;

@Service
public class ChatMessageService {
     @Autowired
    private ChatMessageRepository messageRepository;

    public ChatMessage sendMessage(Account sender, Account receiver, String content) {
        ChatMessage message = new ChatMessage();
        message.setAccountSender(sender);
        message.setAccountReceiver(receiver);
        message.setMessageContent(content);
        message.setTimeStamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<ChatMessage> getMessagesBetween(Account account1, Account account2) {
        // Giả sử bạn có một phương thức truy vấn tin nhắn giữa hai người dùng
        return messageRepository.findByAccountSenderAndAccountReceiverOrderByTimeStampAsc(account1, account2);
    }
    public List<ChatMessage> getMessagesReceiver(Account account) {
        // Giả sử bạn có một phương thức truy vấn tin nhắn giữa hai người dùng
        return messageRepository.findByAccountReceiverOrderByTimeStampAsc(account);
    }
    public List<ChatMessage> getMessagesSender(Account account) {
        // Giả sử bạn có một phương thức truy vấn tin nhắn giữa hai người dùng
        return messageRepository.findByAccountSenderOrderByTimeStampAsc(account);
    }
}
