package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
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
import com.example.passkeeper.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ImageButton openedEyeButton;
    private ImageButton closedEyeButton;
    private TextView passwordStrengthTextView;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = findViewById(R.id.usernameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        openedEyeButton = findViewById(R.id.openedEyeButton);
        closedEyeButton = findViewById(R.id.closedEyeButton);
        passwordStrengthTextView = findViewById(R.id.passwordStrengthTextView);

        passwordEditText.addTextChangedListener(new MyTextWatcher());
    }

    public void signUpButtonClicked(View view) {
        // make trim for inputs
        usernameEditText.setText(usernameEditText.getText().toString().trim());
        phoneEditText.setText(phoneEditText.getText().toString().trim());
        emailEditText.setText(emailEditText.getText().toString().trim());
        passwordEditText.setText(passwordEditText.getText().toString().trim());

        if (validate()) {
            User newUser = new User(emailEditText.getText().toString(), usernameEditText.getText().toString(), phoneEditText.getText().toString());
            String password = passwordEditText.getText().toString();

            auth.createUserWithEmailAndPassword(newUser.getEmail(), password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this, "User Signed up successfully.", Toast.LENGTH_SHORT).show();
                            FirebaseFirestore.getInstance().collection("users").document(newUser.getEmail()).set(newUser);

                            finish();
                            Intent intent = new Intent(this, VerifyNumberActivity.class);
                            intent.putExtra("isNumberVerify", true);
                            intent.putExtra("isEmailVerify", true);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "User already exists.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean validate() {
        // validate that username is just has letters, . and _ using regular expression
        if (!usernameEditText.getText().toString().matches(getString(R.string.username_regular_expression))) {
            Toast.makeText(this, "Username is not valid.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // validate that phone number is 10 digits
        if (phoneEditText.getText().toString().length() != 10) {
            Toast.makeText(this, "Phone number must be 10 digits.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // validate email regular expression
        if (!emailEditText.getText().toString().matches(getString(R.string.email_regular_expression))) {
            Toast.makeText(this, "Email is not valid.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // validate password length is at least 8 characters
        if (passwordStrengthTextView.getText().toString().equals("Weak")) {
            Toast.makeText(this, "Password must be at least 8 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void showPasswordButtonClicked(View view) {
        if (passwordEditText.getInputType() == 129) {
            closedEyeButton.setVisibility(View.GONE);
            openedEyeButton.setVisibility(View.VISIBLE);
            passwordEditText.setInputType(1);
        } else {
            openedEyeButton.setVisibility(View.GONE);
            closedEyeButton.setVisibility(View.VISIBLE);
            passwordEditText.setInputType(129);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String password = passwordEditText.getText().toString();
            PasswordStrengthChecker.PasswordStrengthStatus passwordStraight =
                    PasswordStrengthChecker.checkPasswordStrength(password);

            switch (passwordStraight) {
                case WEAK:
                    passwordStrengthTextView.setText(R.string.weak_password_strength);
                    passwordStrengthTextView.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.red));
                    break;
                case MEDIUM:
                    passwordStrengthTextView.setText(R.string.medium_password_strength);
                    passwordStrengthTextView.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.orange));
                    break;
                case STRONG:
                    passwordStrengthTextView.setText(R.string.strong_password_strength);
                    passwordStrengthTextView.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.green));
                    break;

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}