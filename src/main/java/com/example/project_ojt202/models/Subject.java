package com.example.project_ojt202.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject that = (Subject) o;
        return Objects.equals(subjectID, that.subjectID) && Objects.equals(subjectName, that.subjectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectID, subjectName);
    }

    public Subject(String subjectID, String subjectName) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
    }
}
