package com.example.controller;

import com.example.api.APIResponse;
import com.example.dto.*;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<APIResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest req) {

        RegisterResponse res = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("User registered successfully", res));
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<APIResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequest req) {

        JwtResponse res = authService.login(req);
        return ResponseEntity.ok(
                APIResponse.success("Login successful", res));
    }

    // POST /api/auth/forgot-password
    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest req) {

        authService.forgotPassword(req);
        return ResponseEntity.ok(
                APIResponse.success("Password reset instructions sent"));
    }

    // PUT /api/auth/change-password
    @PutMapping("/change-password")
    public ResponseEntity<APIResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest req,
            Authentication auth) {

        authService.changePassword(auth.getName(), req);
        return ResponseEntity.ok(
                APIResponse.success("Password changed successfully"));
    }
}