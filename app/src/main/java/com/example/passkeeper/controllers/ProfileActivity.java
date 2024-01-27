package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passkeeper.R;
import com.example.passkeeper.models.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    private TextView email;
    private EditText phoneNum;
    private TextView userName;
    private Button logOut;
    private EditText accountPassword;

    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private User user;
    private SharedPreferences sharedPreferences;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser firebaseUser = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        phoneNum = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.emailProfile);
        userName = findViewById(R.id.user_name);
        logOut = findViewById(R.id.Log_out);
        accountPassword = findViewById(R.id.accountPassword);
        sharedPreferences = getSharedPreferences(
                getString(R.string.login)
                , Context.MODE_PRIVATE);

        TextView changePassword = findViewById(R.id.changePassword);
        changePassword.setText(Html.fromHtml("<u>" + getString(R.string.change_password) + "</u>"));
        loadAppInfo();

        phoneNum.addTextChangedListener(new MyTextWatcher());
        changePassword.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ChangeProfilePasswordActivity.class);
            startActivity(intent);


        });

        logOut.setOnClickListener(view -> {

            if (!accountPassword.getText().toString().isEmpty() && logOut.getText().toString().equals(getText(R.string.save_and_log_out).toString())) {
                AuthCredential credential = EmailAuthProvider.getCredential(sharedPreferences.getString("logInEmail", ""), accountPassword.getText().toString());

                firebaseUser.reauthenticate(credential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                finish();
                                // User successfully re-authenticated, now change the password
                                user.setPhoneNumber(phoneNum.getText().toString());
                                user.setPhoneNumberVerified(false);

                                fireStore.collection("users").document(user.getEmail()).set(user);
                                Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();

                            } else {
                                // Handle re-authenticated failure
                                Toast.makeText(this, "Password is wrong.", Toast.LENGTH_SHORT).show();
                            }
                        });

            } else if (accountPassword.getText().toString().isEmpty() && logOut.getText().toString().equals(getText(R.string.save_and_log_out).toString())) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }

        });


    }


    @Override
    public void onBackPressed() {
        // Check if the current activity is the DashboardActivity
        if (getClass().getSimpleName().equals("ProfileActivity")) {
            // You can show a Toast message or take any other action
            Toast.makeText(this, "Back button press disabled on Profile page", Toast.LENGTH_SHORT).show();
        } else {
            // Call the default behavior to allow navigating back for other activities
            super.onBackPressed();
        }
    }

    public void getStartedButtonClicked(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void changePassword(View view) {
        Intent intent = new Intent(this, ChangeProfilePasswordActivity.class);
        startActivity(intent);
    }

    public void openAddPassword(View view) {
        Intent intent = new Intent(this, AddPasswordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        overridePendingTransition(0, 0);
        this.finish();

    }

    public void openHomePage(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        overridePendingTransition(0, 0);
        this.finish();
    }

    private void loadAppInfo() {


        fireStore.collection("users").document(sharedPreferences.getString("logInEmail", "")).get()
                .addOnSuccessListener(documentSnapshot -> {

                    user = User.fromMap(documentSnapshot);
                    userName.setText(user.getUserName());
                    phoneNum.setText(user.getPhoneNumber());
                    email.setText(user.getEmail());


                });
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (user != null && !user.getPhoneNumber().equals(phoneNum.getText().toString())) {
                logOut.setText(getText(R.string.save_and_log_out));
                accountPassword.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}