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
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    private TextView email;
    private EditText phoneNum;
    private TextView userName;
    private Button logOut;
    private EditText accountPassword;

    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        phoneNum = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.emailProfile);
        userName = findViewById(R.id.user_name);
        logOut = findViewById(R.id.Log_out);
        accountPassword = findViewById(R.id.accountPassword);

        TextView changePassword = findViewById(R.id.changePassword);
        changePassword.setText(Html.fromHtml("<u>" + getString(R.string.change_password) + "</u>"));
        loadAppInfo();

        phoneNum.addTextChangedListener(new MyTextWatcher());
        changePassword.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ChangeProfilePasswordActivity.class);
            startActivity(intent);


        });
        logOut.setOnClickListener(view -> {
            finish();
            
            if (logOut.getText().toString().equals(getText(R.string.save_and_log_out).toString())) {
                user.setPhoneNumber(phoneNum.getText().toString());
                user.setPhoneNumberVerified(false);

                fireStore.collection("users").document(user.getEmail()).set(user);
                Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
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

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.login)
                , Context.MODE_PRIVATE);

        fireStore.collection("users").document(sharedPref.getString("logInEmail", "")).get()
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
            if (!user.getPhoneNumber().equals(phoneNum.getText().toString())) {
                logOut.setText(getText(R.string.save_and_log_out));
                accountPassword.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}