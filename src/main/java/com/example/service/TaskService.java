package com.example.service;

import java.util.List;

import com.example.dto.TaskCreateRequest;
import com.example.dto.TaskResponse;
import com.example.dto.TaskStatusRequest;

public interface TaskService {

    TaskResponse create(TaskCreateRequest req);

    List<TaskResponse> getMyTasks(String email, String status);

    TaskResponse getById(Long id);

    TaskResponse updateStatus(Long id, TaskStatusRequest req, String email);

    List<TaskResponse> getAll(String status);
}