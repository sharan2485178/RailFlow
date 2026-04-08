package com.example.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.dto.NotificationCreateRequest;
import com.example.dto.NotificationResponse;
import com.example.enums.NotificationStatus;
import com.example.model.Notification;

@Component
public class NotificationMapper {

    public Notification toEntity(NotificationCreateRequest req) {
        Notification notification = new Notification();
        notification.setUserId(req.getUserId());
        notification.setRole(req.getRole());
        notification.setMessage(req.getMessage());
        notification.setEntityId(req.getEntityId());
        notification.setEntityType(req.getEntityType());
        notification.setCategory(req.getCategory());
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setCreatedAt(LocalDateTime.now());
        return notification;
    }

    public NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setUserId(notification.getUserId());
        response.setRole(notification.getRole());
        response.setMessage(notification.getMessage());
        response.setEntityId(notification.getEntityId());
        response.setEntityType(notification.getEntityType());
        response.setCategory(notification.getCategory());
        response.setStatus(notification.getStatus());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }
}