package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@lombok.Data
public class ForgotPasswordRequest {
    @Email @NotBlank private String email;
}
