package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.APIResponse;
import com.example.dto.NotificationCreateRequest;
import com.example.dto.NotificationResponse;
import com.example.dto.NotificationStatusRequest;
import com.example.service.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * POST /api/notifications
     * Admin creates a notification for any user.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<NotificationResponse>> create(
            @Valid @RequestBody NotificationCreateRequest req) {
        NotificationResponse response = notificationService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Notification created successfully", response));
    }

    /**
     * GET /api/notifications
     * Returns all notifications for the authenticated user.
     * Optional filters: ?status=UNREAD|READ|DISMISSED  ?category=BOOKING|CONFLICT|...
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<APIResponse<List<NotificationResponse>>> getMyNotifications(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            Authentication auth) {
        return ResponseEntity.ok(
                APIResponse.success("Notifications fetched successfully",
                        notificationService.getMyNotifications(auth.getName(), status, category)));
    }

    /**
     * GET /api/notifications/{id}
     * Retrieve a single notification by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<APIResponse<NotificationResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Notification fetched successfully",
                        notificationService.getById(id)));
    }

    /**
     * PUT /api/notifications/{id}/status
     * Receiver marks a notification READ or DISMISSED.
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<APIResponse<NotificationResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody NotificationStatusRequest req,
            Authentication auth) {
        return ResponseEntity.ok(
                APIResponse.success("Notification status updated successfully",
                        notificationService.updateStatus(id, req, auth.getName())));
    }

    /**
     * GET /api/notifications/all
     * Admin view — all notifications across all users.
     * Optional filters: ?status=UNREAD|READ|DISMISSED  ?category=BOOKING|CONFLICT|...
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<NotificationResponse>>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(
                APIResponse.success("All notifications fetched successfully",
                        notificationService.getAll(status, category)));
    }
}