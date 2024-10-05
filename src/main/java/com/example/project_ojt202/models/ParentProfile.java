package com.example.project_ojt202.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PARENTPROFILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParentProfile {
    @Id
    @Column(name = "parentID")
    private String parentID;

    @Column(name = "parentName")
    private String parentName;

    @Column(name = "parentPhone")
    private String parentPhone;

    @Column(name = "parentEmail")
    private String parentEmail;

    @Column(name = "relationship")
    private String relationship;


}