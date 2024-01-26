package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passkeeper.R;
import com.example.passkeeper.models.PasswordStrengthChecker;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeProfilePasswordActivity extends AppCompatActivity {
    private EditText oldPassword;
    private EditText newPassword;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();
    private TextView passwordStrengthTextView;
    private ImageButton openedEyeButton;
    private ImageButton closedEyeButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_profile);
        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        passwordStrengthTextView = findViewById(R.id.changePasswordStrengthTextView);
        openedEyeButton = findViewById(R.id.openedEyeButton);
        closedEyeButton = findViewById(R.id.closedEyeButton);

        sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        newPassword.addTextChangedListener(new ChangeProfilePasswordActivity.MyTextWatcher());


    }

    public void save(View view) {
        if (validate()) {

            AuthCredential credential = EmailAuthProvider.getCredential(sharedPreferences.getString("logInEmail", ""), oldPassword.getText().toString());

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // User successfully re-authenticated, now change the password
                            user.updatePassword(newPassword.getText().toString())
                                    .addOnCompleteListener(passwordUpdateTask -> {
                                        if (passwordUpdateTask.isSuccessful()) {
                                            // Password updated successfully
                                            Toast.makeText(this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Handle password update failure
                                            Toast.makeText(this, "Password update failure.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Handle re-authenticated failure
                            Toast.makeText(this, "Old password is wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean validate() {
        if (oldPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Old password is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // validate password length is at least 8 characters
        if (passwordStrengthTextView.getText().toString().equals("Weak")) {
            Toast.makeText(this, "New password must be at least 8 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    public void showPasswordButtonClicked(View view) {
        if (newPassword.getInputType() == 129) {
            closedEyeButton.setVisibility(View.GONE);
            openedEyeButton.setVisibility(View.VISIBLE);
            newPassword.setInputType(1);
        } else {
            openedEyeButton.setVisibility(View.GONE);
            closedEyeButton.setVisibility(View.VISIBLE);
            newPassword.setInputType(129);
        }

    }

    public void forgotPasswordButtonClicked(View view) {
        auth.sendPasswordResetEmail(sharedPreferences.getString("logInEmail", ""))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Email sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Email not sent.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String password = newPassword.getText().toString();
            PasswordStrengthChecker.PasswordStrengthStatus passwordStraight =
                    PasswordStrengthChecker.checkPasswordStrength(password);

            switch (passwordStraight) {
                case WEAK:
                    passwordStrengthTextView.setText(R.string.weak_password_strength);
                    passwordStrengthTextView.setTextColor(ContextCompat.getColor(ChangeProfilePasswordActivity.this, R.color.red));
                    break;
                case MEDIUM:
                    passwordStrengthTextView.setText(R.string.medium_password_strength);
                    passwordStrengthTextView.setTextColor(ContextCompat.getColor(ChangeProfilePasswordActivity.this, R.color.orange));
                    break;
                case STRONG:
                    passwordStrengthTextView.setText(R.string.strong_password_strength);
                    passwordStrengthTextView.setTextColor(ContextCompat.getColor(ChangeProfilePasswordActivity.this, R.color.green));
                    break;

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}