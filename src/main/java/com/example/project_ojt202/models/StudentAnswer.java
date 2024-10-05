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
@Table(name = "ANSWEREXAMSTUDENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AnswerExamStudentID")
    private Long AnswerExamStudentID;
    
    @Column(name = "AnswerExamStudent")
    private String AnswerExamStudent;

    @Column(name = "score")
    private double score;

    @Column(name = "ResultStatus")
    private boolean ResultStatus;

    @ManyToOne
    @JoinColumn(name = "questionTestID")
    private QuestionTest questionTest;

    @ManyToOne
    @JoinColumn(name = "examID")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "studentID")
    private StudentProfile studentProfile;

}
