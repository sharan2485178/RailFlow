package com.example.controller;

import com.example.dto.TaskCreateRequest;
import com.example.dto.TaskResponse;
import com.example.dto.TaskStatusRequest;
import com.example.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired private TaskService taskService;

    /**
     * POST /api/tasks
     * Admin creates a task and assigns it to a user.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                taskService.create(
                        req.getAssignedTo(), req.getDescription(),
                        req.getRelatedEntityType(), req.getRelatedEntityId(),
                        req.getDueDate()
                )
        );
    }

    /**
     * GET /api/tasks
     * Returns all tasks assigned to the authenticated user.
     * Optional filter: ?status=PENDING|IN_PROGRESS|COMPLETED|OVERDUE
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<List<TaskResponse>> getMyTasks(
            @RequestParam(required = false) String status,
            Authentication auth) {
        return ResponseEntity.ok(taskService.getMyTasks(auth.getName(), status));
    }

    /**
     * GET /api/tasks/{id}
     * Retrieve a single task by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<TaskResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    /**
     * PUT /api/tasks/{id}/status
     * Assignee updates their task status (PENDING → IN_PROGRESS → COMPLETED).
     * Automatically checks sibling completion when marked COMPLETED.
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<TaskResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody TaskStatusRequest req,
            Authentication auth) {
        return ResponseEntity.ok(taskService.updateStatus(id, req, auth.getName()));
    }

    /**
     * GET /api/tasks/all
     * Admin view — all tasks across all users.
     * Optional filter: ?status=PENDING|IN_PROGRESS|COMPLETED|OVERDUE
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskResponse>> getAll(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(taskService.getAll(status));
    }
}
