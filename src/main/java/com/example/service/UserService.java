package com.example.service;

import java.util.List;

import com.example.dto.UpdateUserRequest;
import com.example.model.User;

public interface UserService {
    List<User> getAllUsers(String statusFilter);
    User getUserById(Long userId);
    User updateUser(Long userId, UpdateUserRequest req);
}
