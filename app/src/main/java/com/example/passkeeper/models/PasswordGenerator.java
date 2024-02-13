package com.example.passkeeper.models;

import java.util.Random;

public class PasswordGenerator {
    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SPECIAL_CHARACTERS = "!&*%$#";
    private static final String DIGITS = "0123456789";

    public static String generatePassword(int length) {
        if (length < 4)
            throw new IllegalArgumentException("Password length must be greater than or equal 4 to has lower letter, capital letter, digit and special character.");

        char[] password = new char[length];
        Random random = new Random();

        // add first 4 characters, one from each character set
        password[0] = LOWERCASE_CHARACTERS.charAt(random.nextInt(LOWERCASE_CHARACTERS.length()));
        password[1] = UPPERCASE_CHARACTERS.charAt(random.nextInt(UPPERCASE_CHARACTERS.length()));
        password[2] = DIGITS.charAt(random.nextInt(DIGITS.length()));
        password[3] = SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length()));

        String validChars = "";
        validChars += LOWERCASE_CHARACTERS;
        validChars += UPPERCASE_CHARACTERS;
        validChars += DIGITS;
        validChars += SPECIAL_CHARACTERS;

        for (int i = 4; i < length; i++) {
            int randomIndex = random.nextInt(validChars.length());
            password[i] = validChars.charAt(randomIndex);
        }

        // shuffle the password
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(length);
            char temp = password[i];
            password[i] = password[randomIndex];
            password[randomIndex] = temp;
        }

        return new String(password);
    }

}
