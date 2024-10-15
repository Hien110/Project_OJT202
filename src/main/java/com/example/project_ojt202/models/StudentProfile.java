package com.example.project_ojt202.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "STUDENTPROFILE")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile {
    @Id
    @Column(name = "studentID")
    private String studentID;
    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "address")
    private String address;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "yearOfAdmission")
    private int yearOfAdmission;

    @Column(name = "schoolYear")
    private int schoolYear;


    @ManyToOne
    @JoinColumn(name = "majorID")
    private Major major;

    @OneToOne
    @JoinColumn(name = "parentID")
    private ParentProfile parent;

}
