package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.UserResponse;
import com.example.model.User;

@Component
public class UserMapper {

    public UserResponse toDto(User user) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        res.setStatus(user.getStatus());
        return res;
    }
}