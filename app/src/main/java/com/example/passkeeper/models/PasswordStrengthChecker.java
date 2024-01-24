package com.example.passkeeper.models;

public class PasswordStrengthChecker {

    public enum PasswordStrengthStatus {WEAK, MEDIUM, STRONG}

    public static PasswordStrengthStatus checkPasswordStrength(String password) {
        // Check length
        if (password.length() < 8) {
            return PasswordStrengthStatus.WEAK;
        }

        // Check for both uppercase and lowercase letters
        // when containsUppercase && containsLowercase then it will go to next step
        // when one of them not in string thing it just Medium
        if (!containsUppercase(password) || !containsLowercase(password)) {
            return PasswordStrengthStatus.MEDIUM;
        }

        // Check for at least one digit
        // same as idea of above
        if (!containsDigit(password)) {
            return PasswordStrengthStatus.MEDIUM;
        }

        // when containsUppercase && containsLowercase && containsDigit then it's strong
        return PasswordStrengthStatus.STRONG;
    }

    private static boolean containsUppercase(String password) {
        return !password.equals(password.toLowerCase());
    }

    private static boolean containsLowercase(String password) {
        return !password.equals(password.toUpperCase());
    }

    private static boolean containsDigit(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
}
