package com.example.service;

import com.example.model.User;
import com.example.model.UserStatus;
import com.example.repository.UserRepository;
import com.example.dto.*;
import com.example.security.AuditService;
import com.example.security.EmailService;
import com.example.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuditService auditService;

    @Autowired
    private EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    // In-memory token store — replace with Redis or DB for production
    private final Map<String, String> passwordResetTokens = new ConcurrentHashMap<>();

    public RegisterResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered");

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        auditService.log("REGISTER", "User", user.getId().toString(), req.getEmail(),
            "New user registered with role: " + req.getRole());

        emailService.sendWelcome(user.getEmail(), user.getName(), user.getRole().name());

        return new RegisterResponse(user.getId(), "ACTIVE",
            "Registration successful. You can now log in.");
    }

    public JwtResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid email or password");

        if (user.getStatus() == UserStatus.SUSPENDED)
            throw new RuntimeException("Your account has been suspended. Please contact admin.");

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        auditService.log("LOGIN", "User", user.getId().toString(), user.getEmail(), "User logged in");

        return new JwtResponse(token, user.getRole().name(), user.getEmail(), user.getName());
    }

    public ApiResponse forgotPassword(ForgotPasswordRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
            .orElseThrow(() -> new RuntimeException("No account found with that email"));

        String token = UUID.randomUUID().toString();
        passwordResetTokens.put(token, user.getEmail());
        String resetLink = baseUrl + "/api/auth/reset-password?token=" + token;
        emailService.sendPasswordResetLink(user.getEmail(), resetLink);

        return new ApiResponse(true, "Password reset link sent to your email.");
    }

    public ApiResponse changePassword(String email, ChangePasswordRequest req) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword()))
            throw new RuntimeException("Current password is incorrect");

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        auditService.log("CHANGE_PASSWORD", "User", user.getId().toString(), email, "Password changed");
        return new ApiResponse(true, "Password changed successfully.");
    }
}
