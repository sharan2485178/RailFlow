package com.example.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.dto.TaskCreateRequest;
import com.example.dto.TaskResponse;
import com.example.dto.TaskStatusRequest;
import com.example.enums.NotificationCategory;
import com.example.enums.NotificationStatus;
import com.example.enums.Role;
import com.example.enums.TaskStatus;
import com.example.mapper.TaskMapper;
import com.example.model.Notification;
import com.example.model.Task;
import com.example.model.User;
import com.example.repository.NotificationRepository;
import com.example.repository.TaskRepository;
import com.example.repository.UserRepository;
import com.example.security.AuditService;
import com.example.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository,
                           NotificationRepository notificationRepository,
                           UserRepository userRepository,
                           AuditService auditService,
                           TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.taskMapper = taskMapper;
    }

    public TaskResponse create(TaskCreateRequest req) {
        userRepository.findById(req.getAssignedTo())
                .orElseThrow(() -> new RuntimeException("User not found: " + req.getAssignedTo()));

        Task task = taskMapper.toEntity(req);
        taskRepository.save(task);

        auditService.log("CREATE_TASK", "Task", task.getId().toString(),
                "SYSTEM", "Task assigned to userId=" + req.getAssignedTo() + " due=" + req.getDueDate());

        return taskMapper.toResponse(task);
    }

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

        return tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
        return taskMapper.toResponse(task);
    }

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

        return taskMapper.toResponse(task);
    }

    public List<TaskResponse> getAll(String status) {
        List<Task> tasks;
        if (status != null && !status.isBlank()) {
            tasks = taskRepository.findByStatus(TaskStatus.valueOf(status.toUpperCase()));
        } else {
            tasks = taskRepository.findAll();
        }
        return tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

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
            User owner = userRepository.findById(task.getAssignedTo()).orElse(null);
            Notification ownerNotif = new Notification();
            ownerNotif.setUserId(task.getAssignedTo());
            ownerNotif.setRole(owner.getRole());
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
                adminNotif.setRole(Role.ADMIN);
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