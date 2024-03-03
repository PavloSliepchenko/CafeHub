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
    private static final String PASSWORD_RESET_TEMPLATE = """
            Hello!
                        
            Your new password is: %s
            """;
    private static final String EMAIL_VERIFICATION_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="en">
              <body style="font-family: Georgia">
                <h3 style="text-align: center">Email verification</h3>
                <p>
                    Dear %s,<br>
                    Please verify your email by clicking "verify" button
                </p>
                <a href=%s/users/verify?verificationCode=%s>VERIFY</a>
                <p>
                    Thank you!<br>
                    The CafeHub team
                </p>
              </body>
            </html>
            """;
    private final JavaMailSender mailSender;

    @Override
    public void sendPasswordResetEmail(String toEmail, String newPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(toEmail, "CafeHub");
            helper.setTo(toEmail);
            helper.setSubject("Password reset (CafeHub)");
            helper.setText(String.format(PASSWORD_RESET_TEMPLATE, newPassword));
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException(e);
        }
    }

    @Override
    public void sendVerificationEmail(String toEmail,
                                      String name,
                                      String verificationCode,
                                      String siteUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(toEmail, "CafeHub");
            helper.setTo(toEmail);
            helper.setSubject("Email verification (CafeHub)");
            helper.setText(
                    String.format(EMAIL_VERIFICATION_TEMPLATE, name, siteUrl, verificationCode),
                    true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException(e);
        }
    }
}
