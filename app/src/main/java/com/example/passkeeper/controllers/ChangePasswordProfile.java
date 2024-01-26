package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.example.passkeeper.R;
import com.example.passkeeper.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordProfile extends AppCompatActivity {
    private EditText oldPassword;
    private EditText newPassword;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_profile);
        oldPassword=findViewById(R.id.oldPassword);
        newPassword=findViewById(R.id.newPassword);


    }
    private void loadAppInfo() {

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.login)
                , Context.MODE_PRIVATE);

        fireStore.collection("users").document(sharedPref.getString("logInEmail", "")).get()
                .addOnSuccessListener(documentSnapshot -> {

                    User user = User.fromMap(documentSnapshot);



                });
    }
}