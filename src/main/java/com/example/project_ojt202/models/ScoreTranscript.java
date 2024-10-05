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
@Table(name = "SCORETRANSCRIPT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ScoreTranscript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scoreTranscriptID")
    private Long scoreTranscriptID;

    @Column(name = "nameTest")
    private String nameTest;

    @Column(name = "numberColumn")
    private int numberColumn;

    @Column(name = "totalPercent")
    private int totalPercent;

    @ManyToOne
    @JoinColumn(name = "subjectID")
    private Subject subject;
}
