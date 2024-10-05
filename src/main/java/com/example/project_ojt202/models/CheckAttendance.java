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
@Table(name = "CHECKATTENDANCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CheckAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkAttendanceID")
    private Long checkAttendanceID;

    @Column(name = "checkAttendance")
    private boolean checkAttendance;

    @ManyToOne
    @JoinColumn(name = "scheduceID")
    private Scheduce scheduce;

    @ManyToOne
    @JoinColumn(name = "studentID")
    private StudentProfile studentProfile;
}
