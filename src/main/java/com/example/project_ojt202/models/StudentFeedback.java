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
@Table(name = "STUDENTFEEDBACK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StudentFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentFeedBackID")
    private Long studentFeedBackID;

    @ManyToOne
    @JoinColumn(name = "studentID", referencedColumnName = "studentID")
    private StudentProfile studentProfile; 
    
    @ManyToOne
    @JoinColumn(name = "uniClassId", referencedColumnName = "uniClassId")
    private UniClass uniClass; 

    @ManyToOne
    @JoinColumn(name = "feedbackChoiceID", referencedColumnName = "feedbackChoiceID")
    private FeedbackChoice feedbackChoice; 
    
    @Column(name = "feedbackText", length = 1000) 
    private String feedbackText;
}
