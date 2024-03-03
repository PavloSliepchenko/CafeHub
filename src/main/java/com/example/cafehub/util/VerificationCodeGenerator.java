package com.example.cafehub.util;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component("verificationCodeGenerator")
public class VerificationCodeGenerator implements CodeGenerator {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String PASSWORD_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static final Random random = new Random();

    @Override
    public String generateCode(int length) {
        StringBuilder password = new StringBuilder(length);
        password.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
        password.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
        password.append(NUMBER.charAt(random.nextInt(NUMBER.length())));

        for (int i = 3; i < length; i++) {
            password.append(PASSWORD_BASE.charAt(random.nextInt(PASSWORD_BASE.length())));
        }

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(length);
            char temp = password.charAt(i);
            password.setCharAt(i, password.charAt(randomIndex));
            password.setCharAt(randomIndex, temp);
        }
        return password.toString();
    }
}
