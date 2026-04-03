package com.example.service;

import com.example.dto.NotificationResponse;
import com.example.dto.NotificationStatusRequest;
import com.example.model.*;
import com.example.repository.NotificationRepository;
import com.example.repository.UserRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired private NotificationRepository notificationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AuditService auditService;

    /**
     * Internal method — called by other services to fire a notification for a single user.
     * One call = one row; never batch multiple users into a single notification.
     */
    public NotificationResponse create(Long userId, String role, String message,
                                       Long entityId, NotificationEntityType entityType,
                                       NotificationCategory category) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setRole(role);
        notification.setMessage(message);
        notification.setEntityId(entityId);
        notification.setEntityType(entityType);
        notification.setCategory(category);
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        auditService.log("CREATE_NOTIFICATION", "Notification", notification.getId().toString(),
                "SYSTEM", "Notification sent to userId=" + userId + " [" + category + "]");
        return NotificationResponse.fromNotification(notification);
    }

    /**
     * GET /api/notifications — returns notifications for the currently authenticated user.
     * Optional filters: status, category.
     */
    public List<NotificationResponse> getMyNotifications(String email, String status, String category) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        List<Notification> notifications;

        if (status != null && !status.isBlank() && category != null && !category.isBlank()) {
            notifications = notificationRepository.findByUserIdAndStatusAndCategory(
                    user.getId(),
                    NotificationStatus.valueOf(status.toUpperCase()),
                    NotificationCategory.valueOf(category.toUpperCase())
            );
        } else if (status != null && !status.isBlank()) {
            notifications = notificationRepository.findByUserIdAndStatus(
                    user.getId(), NotificationStatus.valueOf(status.toUpperCase())
            );
        } else if (category != null && !category.isBlank()) {
            notifications = notificationRepository.findByUserIdAndCategory(
                    user.getId(), NotificationCategory.valueOf(category.toUpperCase())
            );
        } else {
            notifications = notificationRepository.findByUserId(user.getId());
        }

        return notifications.stream().map(NotificationResponse::fromNotification).collect(Collectors.toList());
    }

    /**
     * GET /api/notifications/{id} — retrieve a single notification by ID.
     */
    public NotificationResponse getById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        return NotificationResponse.fromNotification(notification);
    }

    /**
     * PUT /api/notifications/{id}/status — receiver marks a notification READ or DISMISSED.
     * Receivers may only change status; they cannot edit content.
     */
    public NotificationResponse updateStatus(Long id, NotificationStatusRequest req, String email) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        if (!notification.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied: notification does not belong to this user");
        }

        NotificationStatus oldStatus = notification.getStatus();
        notification.setStatus(req.getStatus());
        notificationRepository.save(notification);

        auditService.log("UPDATE_NOTIFICATION_STATUS", "Notification", id.toString(),
                email, "Status changed: " + oldStatus + " → " + req.getStatus());
        return NotificationResponse.fromNotification(notification);
    }

    /**
     * GET /api/notifications/all — Admin view: all notifications, optionally filtered.
     */
    public List<NotificationResponse> getAll(String status, String category) {
        List<Notification> notifications;

        if (status != null && !status.isBlank() && category != null && !category.isBlank()) {
            notifications = notificationRepository.findAll().stream()
                    .filter(n -> n.getStatus() == NotificationStatus.valueOf(status.toUpperCase())
                            && n.getCategory() == NotificationCategory.valueOf(category.toUpperCase()))
                    .collect(Collectors.toList());
        } else if (status != null && !status.isBlank()) {
            notifications = notificationRepository.findByStatus(NotificationStatus.valueOf(status.toUpperCase()));
        } else if (category != null && !category.isBlank()) {
            notifications = notificationRepository.findByCategory(NotificationCategory.valueOf(category.toUpperCase()));
        } else {
            notifications = notificationRepository.findAll();
        }

        return notifications.stream().map(NotificationResponse::fromNotification).collect(Collectors.toList());
    }
}
