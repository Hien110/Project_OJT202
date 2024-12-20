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
import lombok.ToString;

@Entity
@Table(name = "TEST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "testID")
    private Long testID;

    @Column(name = "examName")
    private String examName;

    @Column(name = "testStartTime")
    private LocalDateTime testStartTime;

    @Column(name = "testFinishTime")
    private LocalDateTime testFinishTime;

    @Column(name = "testkeyWord")
    private String testkeyWord;

    @Column(name = "testLevel")
    private String testLevel;
    
    @Column(name = "time")
    private int time;

    @Column(name = "questionNumber")
    private int questionNumber;

    @Column(name = "easyQuestion")
    private int easyQuestion;

    @Column(name = "mediumQuestion")
    private int mediumQuestion;

    @Column(name = "hardQuestion")
    private int hardQuestion;

    @Column(name = "statusTest")
    private boolean statusTest;

    @ManyToOne
    @JoinColumn(name = "uniClassID")
    private UniClass uniClass;

    @ManyToOne
    @JoinColumn(name = "scoreTranscriptID")
    private ScoreTranscript scoreTranscript;

}
