package com.example.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.dto.TaskCreateRequest;
import com.example.dto.TaskResponse;
import com.example.enums.TaskStatus;
import com.example.model.Task;

@Component
public class TaskMapper {

    public Task toEntity(TaskCreateRequest req) {
        Task task = new Task();
        task.setAssignedTo(req.getAssignedTo());
        task.setDescription(req.getDescription());
        task.setRelatedEntityType(req.getRelatedEntityType());
        task.setRelatedEntityId(req.getRelatedEntityId());
        task.setDueDate(req.getDueDate());
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        return task;
    }

    public TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setAssignedTo(task.getAssignedTo());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setRelatedEntityId(task.getRelatedEntityId());
        response.setRelatedEntityType(task.getRelatedEntityType());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        return response;
    }
}