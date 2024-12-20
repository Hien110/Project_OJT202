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
import lombok.ToString;

@Entity
@Table(name = "UNICLASS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UniClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uniClassId")  
    private Long uniClassId;         

    @Column(name = "uniClassName")  
    private String uniClassName;      

    @Column(name = "semester")
    private String semester;
   
    @Column(name = "numberStudentMax")
    private String numberStudent;

    @Column(name = "dateStartLearn")
    private LocalDate dateStartLearn;

    @Column(name = "dateEndLearn")
    private LocalDate dateEndLearn;

    @ManyToOne
    @JoinColumn(name = "subjectID")  
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "lectureID")   
    private LectureProfile lectureProfile;
    public boolean isFeedbackAvailable() {
        if (dateEndLearn == null) {
            return false; // Không có ngày kết thúc, không hiển thị feedback
        }
        LocalDate today = LocalDate.now();
        LocalDate feedbackStartDate = dateEndLearn.minusDays(14);
        return today.isAfter(feedbackStartDate) || today.isEqual(feedbackStartDate);
    }
}