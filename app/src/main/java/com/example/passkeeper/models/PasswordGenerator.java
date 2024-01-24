package com.example.passkeeper.models;

import java.util.Random;

public class PasswordGenerator {
    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";

    public static String generatePassword(int length) {
        if (length < 3)
            throw new IllegalArgumentException("Password length must be greater than or equal 3 to has lower letter, capital letter and digit.");

        Random random = new Random();

        StringBuilder password = new StringBuilder();
        password.append(random.nextInt(LOWERCASE_CHARACTERS.length()));
        password.append(random.nextInt(UPPERCASE_CHARACTERS.length()));
        password.append(random.nextInt(DIGITS.length()));

        String validChars = "";
        validChars += LOWERCASE_CHARACTERS;
        validChars += UPPERCASE_CHARACTERS;
        validChars += DIGITS;

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(validChars.length());
            password.append(validChars.charAt(randomIndex));
        }

        return password.toString();
    }

}
