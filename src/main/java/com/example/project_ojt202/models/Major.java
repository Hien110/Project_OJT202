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
@Table(name = "MAJOR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Major {
    @Id
    @Column(name = "majorID")
    private String majorID;
    
    @Column(name = "majorName")
    private String majorName;

}
