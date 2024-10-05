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
@Table(name = "CHATROOMPARTICIPANTS")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ChatRoomParticipants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participantID")
    private Long participantID;

    @Column(name = "joinedDate ")
    @CreationTimestamp 
    private LocalDateTime joinedDate;

    @ManyToOne
    @JoinColumn(name = "chatroomID ")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "accountID")
    private Account account;
}
