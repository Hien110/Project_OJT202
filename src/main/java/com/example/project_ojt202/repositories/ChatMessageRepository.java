package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByAccountSenderAndAccountReceiverOrderByTimeStampAsc(Account accountSender, Account accountReceiver);
    List<ChatMessage> findByAccountSenderOrderByTimeStampAsc(Account accountSender);
    List<ChatMessage> findByAccountReceiverOrderByTimeStampAsc(Account accountSender);
}
