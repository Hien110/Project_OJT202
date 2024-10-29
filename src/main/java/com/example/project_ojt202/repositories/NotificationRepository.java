package com.example.project_ojt202.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project_ojt202.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
