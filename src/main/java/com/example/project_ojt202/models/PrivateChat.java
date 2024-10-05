package com.example.project_ojt202.models;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
@Table(name = "PRIVATECHAT")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class PrivateChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatID")
    private Long chatID ;

    @Column(name = "createDate ")
    @CreationTimestamp 
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "accountID")
    private Account account;
}
