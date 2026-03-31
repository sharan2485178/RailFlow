package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@lombok.Data
public class LoginRequest {
    @NotBlank private String email;
    @NotBlank private String password;
}
