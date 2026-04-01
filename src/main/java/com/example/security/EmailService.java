package com.example.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service to handle automated email notifications.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a password reset email asynchronously to ensure the 
     * user interface remains responsive.
     */
    @Async
    public void sendPasswordResetLink(String toEmail, String resetLink) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(from);
            msg.setTo(toEmail);
            msg.setSubject("RailFlow — Password Reset Request");
            msg.setText(
                "A password reset was requested for your account.\n\n" +
                "Click the link below to reset your password:\n" +
                resetLink + "\n\n" +
                "This link is valid for 30 minutes.\n" +
                "If you did not request this, ignore this email.\n\n" +
                "RailFlow Team"
            );

            mailSender.send(msg);
            
        } catch (Exception e) {
            // Log the error but don't crash the application 
            // since email failure is often non-blocking.
            System.err.println("Email sending failed (non-critical): " + e.getMessage());
        }
    }
}