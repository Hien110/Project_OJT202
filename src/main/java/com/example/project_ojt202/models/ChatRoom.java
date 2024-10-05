package com.example.project_ojt202.models;

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
@Table(name = "CHATROOM")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroomID ")
    private Long chatroomID;

    @Column(name = "chatroomName ")
    private String chatroomName ;

    @Column(name = "chatType")
    private String chatType;

    @Column(name = "createDate")
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "lectureID")
    private LectureProfile lectureProfile;

}
