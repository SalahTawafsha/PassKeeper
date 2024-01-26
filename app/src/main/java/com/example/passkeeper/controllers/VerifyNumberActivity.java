package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passkeeper.R;
import com.example.passkeeper.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class VerifyNumberActivity extends AppCompatActivity {
    private EditText codeDigit1, codeDigit2, codeDigit3, codeDigit4;
    private TextView phoneNumberInVerify;
    private String code;
    private User user;
    private boolean isNumberVerify;
    private boolean isEmailVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number);
        TextView resend_code = findViewById(R.id.Resend_codeVerfiyNumber);

        codeDigit1 = findViewById(R.id.CodeDegit1);
        codeDigit2 = findViewById(R.id.CodeDegit2);
        codeDigit3 = findViewById(R.id.CodeDegit3);
        codeDigit4 = findViewById(R.id.CodeDegit4);
        phoneNumberInVerify = findViewById(R.id.phoneNumberInVerify);

        isNumberVerify = getIntent().getBooleanExtra("isNumberVerify", true);
        isEmailVerify = getIntent().getBooleanExtra("isEmailVerify", true);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.login), MODE_PRIVATE);
        String email = sharedPref.getString("logInEmail", "");

        FirebaseFirestore.getInstance().collection("users").document(email).get().addOnSuccessListener(documentSnapshot -> {
            user = User.fromMap(documentSnapshot);
            phoneNumberInVerify.append(user.getPhoneNumber());

        });


        resend_code.setOnClickListener(view -> send());

        send();

    }

    public void getStartedButtonClicked(View view) {
        String code1 = codeDigit1.getText().toString() + codeDigit2.getText().toString() +
                codeDigit3.getText().toString() + codeDigit4.getText().toString();
        if (code1.equals(code)) {
            if (isNumberVerify) {
                user.setPhoneNumberVerified(true);

                FirebaseFirestore.getInstance().collection("users").document(user.getEmail()).set(user).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Verification Success", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(this, VerifyEmailActivity.class);
                    intent.putExtra("isVerify", isEmailVerify);
                    startActivity(intent);
                });
            } else {
                finish();
                Intent intent = new Intent(this, VerifyEmailActivity.class);
                intent.putExtra("isVerify", isEmailVerify);
                startActivity(intent);
            }
        } else {
            Toast.makeText(VerifyNumberActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateVerificationCode() {
        int code = (int) (Math.random() * 9000) + 1000; // Generate a random 4-digit code
        return String.valueOf(code);
    }

    public void send() {
        code = generateVerificationCode(); // ToDo: send code to user's phone number
        Log.i("ver code", code);
    }

//    public void sendSMS(Context context, String incomingNumber, String sms) {
//        SmsManager smsManager = SmsManager.getDefault();                                      //send sms
//        try {
//            ArrayList<String> parts = smsManager.divideMessage(sms);
//            smsManager.sendMultipartTextMessage(incomingNumber, null, parts, null, null);
//            Log.v("ranjith", "Sms to be sent is " + sms);
//        } catch (Exception e) {
//            Log.v("ranjith", e + "");
//        }
//    }


}