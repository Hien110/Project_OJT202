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
@Table(name = "ANSWERTEST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AnswerTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answerTestID")
    private Long answerTestID;

    @Column(name = "answerTestContent", columnDefinition = "TEXT")
    private String answerTestContent;

    @Column(name = "answerTestTrue")
    private boolean answerTestTrue;

    @ManyToOne
    @JoinColumn(name = "questionTestID")
    private QuestionTest questionTest;
}
