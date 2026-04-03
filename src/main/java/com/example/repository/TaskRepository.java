package com.example.repository;

import com.example.model.NotificationEntityType;
import com.example.model.Task;
import com.example.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(Long assignedTo);
    List<Task> findByAssignedToAndStatus(Long assignedTo, TaskStatus status);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByRelatedEntityIdAndRelatedEntityType(Long relatedEntityId, NotificationEntityType relatedEntityType);
    List<Task> findByStatusAndDueDateBefore(TaskStatus status, LocalDateTime cutoff);
}
