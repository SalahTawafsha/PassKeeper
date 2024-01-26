package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.passkeeper.R;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passkeeper.models.EmailSender;
import com.example.passkeeper.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;


public class VerifyEmailActivity extends AppCompatActivity {

    private EditText codeDigit1, codeDigit2, codeDigit3, codeDigit4;

    private String code;
    private boolean isVerify;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        // Initialize EditTexts
        codeDigit1 = findViewById(R.id.CodeDegit1);
        codeDigit2 = findViewById(R.id.CodeDegit2);
        codeDigit3 = findViewById(R.id.CodeDegit3);
        codeDigit4 = findViewById(R.id.CodeDegit4);
        TextView emailAddressInVerify = findViewById(R.id.emailAddressInVerify);
        isVerify = getIntent().getBooleanExtra("isVerify", true);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.login), MODE_PRIVATE);
        email = sharedPref.getString("logInEmail", "");

        emailAddressInVerify.append(email);

        // Initialize Resend Code TextView
        TextView resendCode = findViewById(R.id.Resend_codeVerfiyNumber);
        send();

        resendCode.setOnClickListener(v -> send());

        // Initialize Continue Button
        MaterialButton continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(v -> submitVerificationCode());
    }

    private String generateVerificationCode() {
        int code = (int) (Math.random() * 9000) + 1000; // Generate a random 4-digit code
        return String.valueOf(code);
    }

    public void send() {
        code = generateVerificationCode(); // ToDo: send code to user's email

        EmailSender.sendEmail(email, "Verification code", "Your verification code is: " + code, this);

        Log.i("ver code", code);
    }

    private void submitVerificationCode() {
        String code1 = codeDigit1.getText().toString() + codeDigit2.getText().toString() +
                codeDigit3.getText().toString() + codeDigit4.getText().toString();
        if (code1.equals(code)) {
            if (isVerify) {

                FirebaseFirestore.getInstance().collection("users").document(email).get().addOnSuccessListener(documentSnapshot -> {
                    User user = User.fromMap(documentSnapshot);

                    user.setEmailVerified(true);

                    FirebaseFirestore.getInstance().collection("users").document(email).set(user).addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Verification Success", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(this, DashboardActivity.class);
                        startActivity(intent);
                    });
                });
            } else {
                finish();
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(VerifyEmailActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }
    }
}