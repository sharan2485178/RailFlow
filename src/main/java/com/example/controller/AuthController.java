package com.example.controller;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.APIResponse;
import com.example.dto.ChangePasswordRequest;
import com.example.dto.ForgotPasswordRequest;
import com.example.dto.JwtResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.RegisterResponse;
import com.example.dto.ResetPasswordRequest;
import com.example.service.AuthService;

import jakarta.validation.Valid;
 
@RestController
@RequestMapping("/api/auth")
public class AuthController {
 
    private final AuthService authService;
 
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
 
   
    @PostMapping("/register")
    public ResponseEntity<APIResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest req) {
 
        RegisterResponse res = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("User registered successfully", res));
    }
 
    
    @PostMapping("/login")
    public ResponseEntity<APIResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequest req) {
 
        JwtResponse res = authService.login(req);
        return ResponseEntity.ok(
                APIResponse.success("Login successful", res));
    }
 
    
    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponse<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest req) {
 
        String token = authService.forgotPassword(req);
        return ResponseEntity.ok(
                APIResponse.success("Use this token to reset your password", token));
    }
 
   
    @PutMapping("/reset-password")
    public ResponseEntity<APIResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest req) {
 
        authService.resetPassword(req);
        return ResponseEntity.ok(
                APIResponse.success("Password reset successfully"));
    }
 
   
    @PutMapping("/change-password")
    public ResponseEntity<APIResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest req,
            Authentication auth) {
 
        authService.changePassword(auth.getName(), req);
        return ResponseEntity.ok(
                APIResponse.success("Password changed successfully"));
    }
}