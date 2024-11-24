package com.example.project_ojt202.models;

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
@Table(name = "FEEDBACKCHOIE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FeedbackChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedbackChoiceID")
    private Long feedbackChoiceID;

    @Column(name = "feedbackChoiceContent")
    private String feedbackChoiceContent;

    @Column(name = "feedbackChoiceNote")
    private String feedbackChoiceNote;

    @ManyToOne
    @JoinColumn(name = "feedbackID", nullable = false) 
    private Feedback feedback;
    
}
