package com.example.cafehub.service.impl;

import com.example.cafehub.exception.EmailException;
import com.example.cafehub.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final String MESSAGE_TEMPLATE = """
            Hello!
                        
            Your new password is: %s
            """;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String toEmail, String newPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(toEmail, "Cafe Hub");
            helper.setTo(toEmail);
            helper.setSubject("Password reset (Cafe Hub)");
            helper.setText(String.format(MESSAGE_TEMPLATE, newPassword));
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException(e);
        }
    }
}
