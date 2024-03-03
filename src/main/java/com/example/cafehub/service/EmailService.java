package com.example.cafehub.service;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String newPassword);

    void sendVerificationEmail(String toEmail,
                               String name,
                               String verificationCode,
                               String siteUrl);
}
