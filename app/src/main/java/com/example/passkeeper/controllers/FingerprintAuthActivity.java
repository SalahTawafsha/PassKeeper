package com.example.passkeeper.controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.passkeeper.R;

import java.util.concurrent.Executor;

public class FingerprintAuthActivity extends AppCompatActivity {


    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private ImageButton fingerprint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_authentication);
        fingerprint = findViewById(R.id.imageButtonFinger);

        createBiometricPrompt();
        createPromptInfo();
        showBiometricPrompt();
        fingerprint.setOnClickListener(v -> showBiometricPrompt());

    }

    private void createBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Fingerprint authentication succeeded

                // Navigate to DashboardActivity
                Intent intent = new Intent(FingerprintAuthActivity.this, DashboardActivity.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Fingerprint authentication failed
                Toast.makeText(FingerprintAuthActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPromptInfo() {
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Place your finger on the sensor")
                .setNegativeButtonText("Cancel")
                .build();
    }

    private void showBiometricPrompt() {
        // Display the biometric prompt
        biometricPrompt.authenticate(promptInfo);
    }

    public void openEmailAuthenticationPage(View view) {
        Intent intent = new Intent(this, VerifyEmailActivity.class);
        finish();
        startActivity(intent);
    }
}
