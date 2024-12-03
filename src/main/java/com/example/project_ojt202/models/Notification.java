package com.example.project_ojt202.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "NOTIFICATION")
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationID")
    private Long notificationID;

    @Column(name = "notificationName", columnDefinition = "TEXT")
    private String notificationName;

    @Column(name = "notificationContent", columnDefinition = "TEXT")
    private String notificationContent;

    @Column(name = "notificationFile", columnDefinition = "TEXT")
    private String notificationFile;

    @Column(name = "notificationImage", columnDefinition = "TEXT")
    private String notificationImage;

    @Column(name = "notificationDate")
    private LocalDate notificationDate;

    @Transient
    private String formattedDate;

    public String getFormattedDate() {
        if (notificationDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            return notificationDate.format(formatter);
        }
        return "No Date Available";
    }
}
