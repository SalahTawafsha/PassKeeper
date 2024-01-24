package com.example.passkeeper.models;

public class StringRotationCipher {

    public static String encrypt(String message, int shift) {
        StringBuilder encryptedMessage = new StringBuilder();

        for (char character : message.toCharArray()) {
            // Perform rotation on each character
            char encryptedChar = (char) ((character + shift) % 256);
            encryptedMessage.append(encryptedChar);
        }

        return encryptedMessage.toString();
    }

    public static String decrypt(String encryptedMessage, int shift) {
        // Decryption is the same as encryption with a negative shift
        return encrypt(encryptedMessage, -shift);
    }

}
