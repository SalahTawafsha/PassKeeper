package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.passkeeper.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView changePassword = findViewById(R.id.changePassword);
        changePassword.setText(Html.fromHtml("<u>" + getString(R.string.change_password) + "</u>"));

    }

    public void getStartedButtonClicked(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void changePassword(View view) {
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
}