package com.example.repository;

import com.example.model.Notification;
import com.example.model.NotificationCategory;
import com.example.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByUserIdAndStatus(Long userId, NotificationStatus status);
    List<Notification> findByUserIdAndCategory(Long userId, NotificationCategory category);
    List<Notification> findByUserIdAndStatusAndCategory(Long userId, NotificationStatus status, NotificationCategory category);
    List<Notification> findByStatus(NotificationStatus status);
    List<Notification> findByCategory(NotificationCategory category);
}
