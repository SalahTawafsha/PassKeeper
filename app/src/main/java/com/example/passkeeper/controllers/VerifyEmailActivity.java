package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.passkeeper.R;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class VerifyEmailActivity extends AppCompatActivity {

    private EditText codeDigit1, codeDigit2, codeDigit3, codeDigit4;
    private TextView resendCode;
    private MaterialButton continueButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        // Initialize EditTexts
        codeDigit1 = findViewById(R.id.CodeDegit1);
        codeDigit2 = findViewById(R.id.CodeDegit2);
        codeDigit3 = findViewById(R.id.CodeDegit3);
        codeDigit4 = findViewById(R.id.CodeDegit4);
        mAuth = FirebaseAuth.getInstance();

        // Initialize Resend Code TextView
        resendCode = findViewById(R.id.Resend_codeVerfiyNumber);
        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationEmail();
            }
        });

        // Initialize Continue Button
        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVerificationCode();
            }
        });
    }

    private void resendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerifyEmailActivity.this,
                                    "Verification email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VerifyEmailActivity.this,
                                    "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void submitVerificationCode() {
        String code = codeDigit1.getText().toString() + codeDigit2.getText().toString() +
                codeDigit3.getText().toString() + codeDigit4.getText().toString();

        if (validateCode(code)) {
            // Proceed with the code verification
            // For example, check the code against a backend or a predefined code
            Toast.makeText(this, "Verification Successful", Toast.LENGTH_SHORT).show();
            // Intent to navigate to the next activity
        } else {
            // Show error message
            Toast.makeText(this, "Invalid Verification Code", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateCode(String code) {
        // Basic validation to check if the code is 4 digits
        // More complex validation can be added here
        return code.length() == 4;
    }
}