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
@Table(name = "PREREQUISITESUBJECT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PrerequisiteSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prerequisiteID")
    private Long prerequisiteID;
    
    @Column(name = "prerequisiteSubjectID")
    private String prerequisiteSubjectID;

    @ManyToOne
    @JoinColumn(name = "subjectID")
    private Subject subject;
}
