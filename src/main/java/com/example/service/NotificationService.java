package com.example.service;

import java.util.List;

import com.example.dto.NotificationCreateRequest;
import com.example.dto.NotificationResponse;
import com.example.dto.NotificationStatusRequest;

public interface NotificationService {

    NotificationResponse create(NotificationCreateRequest req);

    List<NotificationResponse> getMyNotifications(String email, String status, String category);

    NotificationResponse getById(Long id);

    NotificationResponse updateStatus(Long id, NotificationStatusRequest req, String email);

    List<NotificationResponse> getAll(String status, String category);
}