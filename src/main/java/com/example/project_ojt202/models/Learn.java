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
@Table(name = "LEARN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Learn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learnID")
    private Long learnID;

    @Column(name = "learnResult")
    private String learnResult;

    @ManyToOne
    @JoinColumn(name = "studentID")
    private StudentProfile studentProfile;

    @ManyToOne
    @JoinColumn(name = "classID")
    private UniClass uniClass;
}
