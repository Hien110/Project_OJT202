package com.example.project_ojt202.models;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRIVATECHATMESSAGE")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class PrivateChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageID")
    private Long messageID;

    @Column(name = "messageContent", columnDefinition = "TEXT")
    private String  messageContent;

    @Column(name = "filePath", columnDefinition = "TEXT")
    private String filePath;

    @Column(name = "timeStamp")
    @CreationTimestamp 
    private LocalDateTime timeStamp;

    @ManyToOne
    @JoinColumn(name = "accountID")
    private Account accountSender;

    @ManyToOne
    @JoinColumn(name = "chatID")
    private PrivateChat privateChat;
}
