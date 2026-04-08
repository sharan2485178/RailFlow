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
import com.example.dto.TaskCreateRequest;
import com.example.dto.TaskResponse;
import com.example.dto.TaskStatusRequest;
import com.example.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * POST /api/tasks
     * Admin creates a task and assigns it to a user.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<TaskResponse>> create(
            @Valid @RequestBody TaskCreateRequest req) {
        TaskResponse response = taskService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Task created successfully", response));
    }

    /**
     * GET /api/tasks
     * Returns all tasks assigned to the authenticated user.
     * Optional filter: ?status=PENDING|IN_PROGRESS|COMPLETED|OVERDUE
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<APIResponse<List<TaskResponse>>> getMyTasks(
            @RequestParam(required = false) String status,
            Authentication auth) {
        return ResponseEntity.ok(
                APIResponse.success("Tasks fetched successfully",
                        taskService.getMyTasks(auth.getName(), status)));
    }

    /**
     * GET /api/tasks/{id}
     * Retrieve a single task by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<APIResponse<TaskResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Task fetched successfully", taskService.getById(id)));
    }

    /**
     * PUT /api/tasks/{id}/status
     * Assignee updates their task status (PENDING → IN_PROGRESS → COMPLETED).
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','ENGINEER','YARD_MANAGER','MAINTENANCE','DRIVER','USER','AUDITOR')")
    public ResponseEntity<APIResponse<TaskResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody TaskStatusRequest req,
            Authentication auth) {
        return ResponseEntity.ok(
                APIResponse.success("Task status updated successfully",
                        taskService.updateStatus(id, req, auth.getName())));
    }

    /**
     * GET /api/tasks/all
     * Admin view — all tasks across all users.
     * Optional filter: ?status=PENDING|IN_PROGRESS|COMPLETED|OVERDUE
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<TaskResponse>>> getAll(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(
                APIResponse.success("All tasks fetched successfully", taskService.getAll(status)));
    }
}