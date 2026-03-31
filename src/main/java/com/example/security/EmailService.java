package com.example.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWelcome(String toEmail, String name, String role) {
        sendEmail(toEmail, "Welcome to RailFlow!",
            "Dear " + name + ",\n\n" +
            "Your account has been created successfully.\n" +
            "Role: " + role + "\n\n" +
            "You can now log in at any time.\n\n" +
            "RailFlow Team");
    }

    @Async
    public void sendAccountSuspended(String toEmail, String name, String reason) {
        sendEmail(toEmail, "RailFlow — Account Suspended",
            "Dear " + name + ",\n\n" +
            "Your account has been suspended.\n" +
            "Reason: " + reason + "\n\n" +
            "Please contact your administrator for assistance.\n\n" +
            "RailFlow Team");
    }

    @Async
    public void sendAccountReactivated(String toEmail, String name) {
        sendEmail(toEmail, "RailFlow — Account Reactivated",
            "Dear " + name + ",\n\n" +
            "Your account has been reactivated. You can now log in.\n\n" +
            "RailFlow Team");
    }

    @Async
    public void sendUsernameReminder(String toEmail, String name) {
        sendEmail(toEmail, "RailFlow — Your Login Email",
            "Dear " + name + ",\n\n" +
            "Your registered login email is: " + toEmail + "\n\n" +
            "RailFlow Team");
    }

    @Async
    public void sendPasswordResetLink(String toEmail, String resetLink) {
        sendEmail(toEmail, "RailFlow — Password Reset Request",
            "A password reset was requested for your account.\n\n" +
            "Click the link below to reset your password:\n" +
            resetLink + "\n\n" +
            "This link is valid for 30 minutes.\n" +
            "If you did not request this, ignore this email.\n\n" +
            "RailFlow Team");
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(from);
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("Email sending failed (non-critical): " + e.getMessage());
        }
    }
}
