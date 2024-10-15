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
@Table(name = "UNICLASS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uniClassId")  
    private Long uniClassId;         

    @Column(name = "uniClassName")  
    private String uniClassName;      

    @Column(name = "semester")
    private String semester;
    
    @Column(name = "uniClassRoom")
    private String uniClassRoom;

    @ManyToOne
    @JoinColumn(name = "subjectID")  
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "lectureID")   
    private LectureProfile lectureProfile;
}
