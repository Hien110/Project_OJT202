package com.example.project_ojt202.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "PREREQUISITESUBJECT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PrerequisiteSubject {
    @Id
    @Column(name = "prerequisiteSubjectID")
    private String prerequisiteSubjectID;

    @ManyToOne
    @JoinColumn(name = "subjectID")
    private Subject subject;
}
