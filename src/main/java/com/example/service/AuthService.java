package com.example.service;

import com.example.dto.ChangePasswordRequest;
import com.example.dto.ForgotPasswordRequest;
import com.example.dto.JwtResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest req);
    JwtResponse login(LoginRequest req);
    void forgotPassword(ForgotPasswordRequest req);
    void changePassword(String email, ChangePasswordRequest req);
}
