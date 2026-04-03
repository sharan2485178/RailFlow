package com.example.controller;

import com.example.dto.NotificationCreateRequest;
import com.example.dto.NotificationResponse;
import com.example.dto.NotificationStatusRequest;
import com.example.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired private NotificationService notificationService;

    /**
     * POST /api/notifications
     * Admin creates a notification for any user.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> create(
            @Valid @RequestBody NotificationCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                notificationService.create(
                        req.getUserId(), req.getRole(), req.getMessage(),
                        req.getEntityId(), req.getEntityType(), req.getCategory()
                )
        );
    }

    /**
     * GET /api/notifications
     * Returns all notifications for the authenticated user.
     * Optional filters: ?status=UNREAD|READ|DISMISSED  ?category=BOOKING|CONFLICT|...
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            Authentication auth) {
        return ResponseEntity.ok(notificationService.getMyNotifications(auth.getName(), status, category));
    }

    /**
     * GET /api/notifications/{id}
     * Retrieve a single notification by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<NotificationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getById(id));
    }

    /**
     * PUT /api/notifications/{id}/status
     * Receiver marks a notification READ or DISMISSED.
     * Content cannot be edited — status only.
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<NotificationResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody NotificationStatusRequest req,
            Authentication auth) {
        return ResponseEntity.ok(notificationService.updateStatus(id, req, auth.getName()));
    }

    /**
     * GET /api/notifications/all
     * Admin view — all notifications across all users.
     * Optional filters: ?status=UNREAD|READ|DISMISSED  ?category=BOOKING|CONFLICT|...
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(notificationService.getAll(status, category));
    }
}
