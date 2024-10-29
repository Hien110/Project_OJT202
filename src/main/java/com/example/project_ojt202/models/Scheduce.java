package com.example.project_ojt202.models;

import java.time.LocalDate;

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
@Table(name = "SCHEDUCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Scheduce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduceID")
    private Long scheduceID;

    @Column(name = "dateScheduce")
    private LocalDate dateScheduce;

    @Column(name = "timeScheduce")
    private String timeScheduce;

    @ManyToOne
    @JoinColumn(name = "uniClassId")
    private UniClass uniClass;
}
