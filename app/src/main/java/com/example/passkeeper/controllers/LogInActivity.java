package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.passkeeper.R;
import com.example.passkeeper.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private ImageButton openedEyeButton;
    private ImageButton closedEyeButton;

    private SharedPreferences.Editor editor;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
       // auth.sendPasswordResetEmail()

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        openedEyeButton = findViewById(R.id.openedEyeButton);
        closedEyeButton = findViewById(R.id.closedEyeButton);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.login), MODE_PRIVATE);
        editor = sharedPref.edit();


        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setPaintFlags(signUpButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void loginButtonClicked(View view) {
        // make trim for inputs
        emailEditText.setText(emailEditText.getText().toString().trim());
        passwordEditText.setText(passwordEditText.getText().toString().trim());

        if (validate()) {
            auth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseFirestore.getInstance().collection("users").document(emailEditText.getText().toString()).get().addOnSuccessListener(documentSnapshot -> {
                        User user = User.fromMap(documentSnapshot);


                        editor.putString("logInEmail", emailEditText.getText().toString());
                        editor.apply();
                        Intent intent = new Intent(this, VerifyNumberActivity.class);
                        if (user.isPhoneNumberVerified() && user.isEmailVerified()) {
                            intent.putExtra("isNumberVerify", false); // make auth
                            intent.putExtra("isEmailVerify", false); // make auth
                        } else if (user.isPhoneNumberVerified()) {
                            intent.putExtra("isNumberVerify", false); // make auth
                            intent.putExtra("isEmailVerify", true); // make verify
                        } else if (user.isEmailVerified()) {
                            intent.putExtra("isNumberVerify", true);
                            intent.putExtra("isEmailVerify", false);
                        } else {
                            intent.putExtra("isNumberVerify", true);
                            intent.putExtra("isEmailVerify", true);
                        }
                        startActivity(intent);
                    });
                } else {
                    emailEditText.setError("Email or password is incorrect");
                }
            });
        } else {
            emailEditText.setError("Email or password is incorrect");
        }

    }
    

    private boolean validate() {
        return !emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty();
    }

    public void openSignUpButtonClicked(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
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
}