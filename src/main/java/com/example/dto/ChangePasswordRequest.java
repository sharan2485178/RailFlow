package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@lombok.Data
public class ChangePasswordRequest {
    @NotBlank private String currentPassword;
    @NotBlank @Size(min = 8) private String newPassword;
}
