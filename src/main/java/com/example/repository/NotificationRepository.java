package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.NotificationCategory;
import com.example.enums.NotificationStatus;
import com.example.model.Notification;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByUserIdAndStatus(Long userId, NotificationStatus status);
    List<Notification> findByUserIdAndCategory(Long userId, NotificationCategory category);
    List<Notification> findByUserIdAndStatusAndCategory(Long userId, NotificationStatus status, NotificationCategory category);
    List<Notification> findByStatus(NotificationStatus status);
    List<Notification> findByCategory(NotificationCategory category);
}
