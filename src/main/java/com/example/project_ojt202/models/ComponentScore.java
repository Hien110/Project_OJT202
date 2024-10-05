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
@Table(name = "COMPONENTSCORE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ComponentScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "componentScoreID")
    private Long componentScoreID;

    @Column(name = "componentScoreValue")
    private double componentScoreValue;

    @ManyToOne
    @JoinColumn(name = "scoreTranscriptID")
    private ScoreTranscript scoreTranscript;
    
    @ManyToOne
    @JoinColumn(name = "learnID")
    private Learn learn;

}
