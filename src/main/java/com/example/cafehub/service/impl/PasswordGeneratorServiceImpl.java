package com.example.cafehub.service.impl;

import com.example.cafehub.service.PasswordGeneratorService;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class PasswordGeneratorServiceImpl implements PasswordGeneratorService {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*_+-=?.";
    private static final String PASSWORD_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
    private static final Random random = new Random();

    @Override
    public String generateRandomPassword(int length) {
        StringBuilder password = new StringBuilder(length);
        password.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
        password.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
        password.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
        password.append(OTHER_CHAR.charAt(random.nextInt(OTHER_CHAR.length())));

        for (int i = 4; i < length; i++) {
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
