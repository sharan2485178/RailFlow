package com.example.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dto.NotificationCreateRequest;
import com.example.dto.NotificationResponse;
import com.example.dto.NotificationStatusRequest;
import com.example.enums.NotificationCategory;
import com.example.enums.NotificationStatus;
import com.example.mapper.NotificationMapper;
import com.example.model.Notification;
import com.example.model.User;
import com.example.repository.NotificationRepository;
import com.example.repository.UserRepository;
import com.example.security.AuditService;
import com.example.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UserRepository userRepository,
                                   AuditService auditService,
                                   NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.notificationMapper = notificationMapper;
    }

    public NotificationResponse create(NotificationCreateRequest req) {
        userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + req.getUserId()));

        Notification notification = notificationMapper.toEntity(req);
        notificationRepository.save(notification);

        auditService.log("CREATE_NOTIFICATION", "Notification", notification.getId().toString(),
                "SYSTEM", "Notification sent to userId=" + req.getUserId() + " [" + req.getCategory() + "]");

        return notificationMapper.toResponse(notification);
    }

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

        return notifications.stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    public NotificationResponse getById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        return notificationMapper.toResponse(notification);
    }

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

        return notificationMapper.toResponse(notification);
    }

    public List<NotificationResponse> getAll(String status, String category) {
        List<Notification> notifications;

        if (status != null && !status.isBlank() && category != null && !category.isBlank()) {
            notifications = notificationRepository.findAll().stream()
                    .filter(n -> n.getStatus() == NotificationStatus.valueOf(status.toUpperCase())
                            && n.getCategory() == NotificationCategory.valueOf(category.toUpperCase()))
                    .collect(Collectors.toList());
        } else if (status != null && !status.isBlank()) {
            notifications = notificationRepository.findByStatus(
                    NotificationStatus.valueOf(status.toUpperCase()));
        } else if (category != null && !category.isBlank()) {
            notifications = notificationRepository.findByCategory(
                    NotificationCategory.valueOf(category.toUpperCase()));
        } else {
            notifications = notificationRepository.findAll();
        }

        return notifications.stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }
}