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
@Table(name = "AVARAGESCORE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AvarageScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avarageScoreID")
    private Long avarageScoreID;

    @Column(name = "avarageScore")
    private double avarageScore;

    @ManyToOne
    @JoinColumn(name = "learnID")
    private Learn learn;
}
