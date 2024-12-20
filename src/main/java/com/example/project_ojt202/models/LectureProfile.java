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

import java.time.LocalDate;
@Entity
@Table(name = "LECTUREPROFILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class LectureProfile {
    @Id
    @Column(name = "lectureID")
    private String lectureID;
    
    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "leadmajor")
    private boolean leadMajor;

    @Column(name = "status")
    private String status;

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

    @ManyToOne
    @JoinColumn(name = "majorID")
    private Major major;
    @Column(name = "avatar")
    private String avatar;
    
       // Override toString() to avoid recursion
       @Override
       public String toString() {
           return "LectureProfile{" +
                  "lectureID='" + lectureID + '\'' +
                  ", firstName='" + firstName + '\'' +
                  ", lastName='" + lastName + '\'' +
                  ", dob=" + dob +
                  '}';
       }
}
