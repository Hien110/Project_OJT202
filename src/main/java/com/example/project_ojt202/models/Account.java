package com.example.project_ojt202.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ACCOUNT")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Account {
    @Id
    @Column(name = "accountID")
    private String accountID;

    @OneToOne
    @JoinColumn(name = "studentID")
    private StudentProfile studentProfile;

    @OneToOne
    @JoinColumn(name = "parentID")
    private ParentProfile parentProfile;

    @OneToOne
    @JoinColumn(name = "lectureID")
    private LectureProfile lectureProfile;

    @Column(name = "accountPassword")
    private String accountPassword;

    @Column(name = "accountRole")
    private String accountRole;
    
    @Column(name = "avatar")
    private String avatar;

}
