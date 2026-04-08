package com.example.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.UpdateUserRequest;
import com.example.enums.UserStatus;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.AuditService;
import com.example.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    public List<User> getAllUsers(String statusFilter) {
        if (statusFilter != null && !statusFilter.isBlank()) {
            return userRepository.findByStatus(UserStatus.valueOf(statusFilter.toUpperCase()));
        }
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }

    public User updateUser(Long userId, UpdateUserRequest req) {
        User user = getUserById(userId);
        if (req.getName() != null) user.setName(req.getName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        auditService.log("UPDATE_USER", "User", userId.toString(), "admin", "User profile updated");
        return user;
    }
}
