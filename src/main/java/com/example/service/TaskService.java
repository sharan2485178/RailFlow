package com.example.service;

import com.example.dto.TaskResponse;
import com.example.dto.TaskStatusRequest;
import com.example.model.*;
import com.example.repository.NotificationRepository;
import com.example.repository.TaskRepository;
import com.example.repository.UserRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AuditService auditService;

    /**
     * Internal method — called by other services to create an actionable task for a user.
     * Every task must have a dueDate — open-ended tasks are not actionable.
     */
    public TaskResponse create(Long assignedTo, String description,
                               NotificationEntityType relatedEntityType,
                               Long relatedEntityId, LocalDateTime dueDate) {
        userRepository.findById(assignedTo)
                .orElseThrow(() -> new RuntimeException("User not found: " + assignedTo));

        Task task = new Task();
        task.setAssignedTo(assignedTo);
        task.setDescription(description);
        task.setStatus(TaskStatus.PENDING);
        task.setRelatedEntityType(relatedEntityType);
        task.setRelatedEntityId(relatedEntityId);
        task.setDueDate(dueDate);
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);

        auditService.log("CREATE_TASK", "Task", task.getId().toString(),
                "SYSTEM", "Task assigned to userId=" + assignedTo + " due=" + dueDate);
        return TaskResponse.fromTask(task);
    }

    /**
     * GET /api/tasks — returns tasks assigned to the currently authenticated user.
     * Optional filter: status.
     */
    public List<TaskResponse> getMyTasks(String email, String status) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        List<Task> tasks;
        if (status != null && !status.isBlank()) {
            tasks = taskRepository.findByAssignedToAndStatus(
                    user.getId(), TaskStatus.valueOf(status.toUpperCase())
            );
        } else {
            tasks = taskRepository.findByAssignedTo(user.getId());
        }

        return tasks.stream().map(TaskResponse::fromTask).collect(Collectors.toList());
    }

    /**
     * GET /api/tasks/{id} — retrieve a single task by ID.
     */
    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
        return TaskResponse.fromTask(task);
    }

    /**
     * PUT /api/tasks/{id}/status — assignee updates their task status.
     * When all sibling tasks for the same relatedEntityId are COMPLETED,
     * the completion is recorded via audit for the parent service to act on.
     */
    public TaskResponse updateStatus(Long id, TaskStatusRequest req, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        if (!task.getAssignedTo().equals(user.getId())) {
            throw new RuntimeException("Access denied: task is not assigned to this user");
        }

        TaskStatus oldStatus = task.getStatus();
        task.setStatus(req.getStatus());
        taskRepository.save(task);

        auditService.log("UPDATE_TASK_STATUS", "Task", id.toString(),
                email, "Status changed: " + oldStatus + " → " + req.getStatus());

        if (req.getStatus() == TaskStatus.COMPLETED) {
            checkSiblingCompletion(task);
        }

        return TaskResponse.fromTask(task);
    }

    /**
     * GET /api/tasks/all — Admin view: all tasks, optionally filtered by status.
     */
    public List<TaskResponse> getAll(String status) {
        List<Task> tasks;
        if (status != null && !status.isBlank()) {
            tasks = taskRepository.findByStatus(TaskStatus.valueOf(status.toUpperCase()));
        } else {
            tasks = taskRepository.findAll();
        }
        return tasks.stream().map(TaskResponse::fromTask).collect(Collectors.toList());
    }

    /**
     * Scheduled job — runs every 5 minutes.
     * Marks PENDING tasks as OVERDUE when their dueDate has passed,
     * and sends an overdue notification to the task owner and Admin.
     */
    @Scheduled(fixedDelay = 300000)
    public void markOverdueTasks() {
        List<Task> overdue = taskRepository.findByStatusAndDueDateBefore(
                TaskStatus.PENDING, LocalDateTime.now()
        );
        for (Task task : overdue) {
            task.setStatus(TaskStatus.OVERDUE);
            taskRepository.save(task);

            auditService.log("TASK_OVERDUE", "Task", task.getId().toString(),
                    "SYSTEM", "Task marked OVERDUE: " + task.getDescription());

            // Notify the task owner
            Notification ownerNotif = new Notification();
            ownerNotif.setUserId(task.getAssignedTo());
            ownerNotif.setRole("ASSIGNEE");
            ownerNotif.setMessage("Your task is overdue: " + task.getDescription());
            ownerNotif.setEntityId(task.getId());
            ownerNotif.setEntityType(task.getRelatedEntityType());
            ownerNotif.setCategory(NotificationCategory.MAINTENANCE);
            ownerNotif.setStatus(NotificationStatus.UNREAD);
            ownerNotif.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(ownerNotif);

            // Notify all Admins
            List<User> admins = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == Role.ADMIN)
                    .collect(Collectors.toList());
            for (User admin : admins) {
                Notification adminNotif = new Notification();
                adminNotif.setUserId(admin.getId());
                adminNotif.setRole("ADMIN");
                adminNotif.setMessage("Task " + task.getId() + " is overdue: " + task.getDescription());
                adminNotif.setEntityId(task.getId());
                adminNotif.setEntityType(task.getRelatedEntityType());
                adminNotif.setCategory(NotificationCategory.MAINTENANCE);
                adminNotif.setStatus(NotificationStatus.UNREAD);
                adminNotif.setCreatedAt(LocalDateTime.now());
                notificationRepository.save(adminNotif);
            }
        }
    }

    /**
     * Checks if all sibling tasks for the same relatedEntityId + relatedEntityType are COMPLETED.
     * Logs an audit event so parent services can advance the entity status automatically.
     */
    private void checkSiblingCompletion(Task completedTask) {
        List<Task> siblings = taskRepository.findByRelatedEntityIdAndRelatedEntityType(
                completedTask.getRelatedEntityId(), completedTask.getRelatedEntityType()
        );
        boolean allDone = siblings.stream()
                .allMatch(t -> t.getStatus() == TaskStatus.COMPLETED);
        if (allDone) {
            auditService.log("ALL_TASKS_COMPLETED", completedTask.getRelatedEntityType().name(),
                    completedTask.getRelatedEntityId().toString(), "SYSTEM",
                    "All tasks completed for " + completedTask.getRelatedEntityType()
                            + " id=" + completedTask.getRelatedEntityId());
        }
    }
}
