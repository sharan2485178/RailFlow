package com.example.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.NotificationEntityType;
import com.example.enums.TaskStatus;
import com.example.model.Task;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(Long assignedTo);
    List<Task> findByAssignedToAndStatus(Long assignedTo, TaskStatus status);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByRelatedEntityIdAndRelatedEntityType(Long relatedEntityId, NotificationEntityType relatedEntityType);
    List<Task> findByStatusAndDueDateBefore(TaskStatus status, LocalDateTime cutoff);
}
