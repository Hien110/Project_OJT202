package com.example.project_ojt202.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "SUBJECT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @Column(name = "subjectID")
    private String subjectID;

    @Column(name = "subjectName")
    private String subjectName;

    @Column(name = "subjectCredit")
    private int subjectCredit;

    @Column(name = "ternNo")
    private int ternNo;

    @ManyToOne
    @JoinColumn(name = "majorID",  nullable = false)
    private Major major;
}
