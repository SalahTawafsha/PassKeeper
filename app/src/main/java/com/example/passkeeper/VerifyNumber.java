package com.example.passkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class VerifyNumber extends AppCompatActivity {
 private TextView Resend_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number);
        Resend_code=findViewById(R.id.Resend_codeVerfiyNumber);
        Resend_code.setText(Html.fromHtml("<u>Your Underlined Text</u>"));


    }
}