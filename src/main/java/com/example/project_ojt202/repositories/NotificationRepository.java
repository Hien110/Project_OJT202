package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.project_ojt202.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByOrderByNotificationDateDesc();

    List<Notification> findAllByOrderByNotificationIDDesc();
    // @Query(value = "SELECT * FROM NOTIFICATION ORDER BY notificationID DESC", nativeQuery = true)
    // List<Notification> findAllByNewestFirst();
}
