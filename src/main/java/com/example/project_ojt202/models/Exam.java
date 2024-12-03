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
@Table(name = "EXAM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examID")
    private Long examID;

    @Column(name = "examScore")
    private Long examScore;

    @ManyToOne
    @JoinColumn(name = "studentID")
    private StudentProfile studentProfile;

    @ManyToOne
    @JoinColumn(name = "testID")
    private Test test;
}
