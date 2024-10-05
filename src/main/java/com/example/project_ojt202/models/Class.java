package com.example.project_ojt202.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CLASS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classID")
    private Long classID;

    @Column(name = "className")
    private String className;

    @OneToOne
    @JoinColumn(name = "subjectID")
    private Subject subject;

    @OneToOne
    @JoinColumn(name = "lectureID")
    private LectureProfile lectureProfile;
}
