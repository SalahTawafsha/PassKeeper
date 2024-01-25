package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passkeeper.R;
import com.example.passkeeper.models.App;
import com.google.android.material.button.MaterialButton;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class VerifyNumberActivity extends AppCompatActivity {
 private TextView Resend_code;
 private EditText codeDigit1, codeDigit2, codeDigit3, codeDigit4;


 String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number);
        Resend_code=findViewById(R.id.Resend_codeVerfiyNumber);

        codeDigit1 = findViewById(R.id.CodeDegit1);
        codeDigit2 = findViewById(R.id.CodeDegit2);
        codeDigit3 = findViewById(R.id.CodeDegit3);
        codeDigit4 = findViewById(R.id.CodeDegit4);
        Resend_code.setOnClickListener(view -> {
            sendSMS(this, "+970594153842",generateVerificationCode());
        });









    }
    public void getStartedButtonClicked(View view) {
        String code1 = codeDigit1.getText().toString() + codeDigit2.getText().toString() +
                codeDigit3.getText().toString() + codeDigit4.getText().toString();
        if(code1.equals(code)){
            Toast.makeText(VerifyNumberActivity.this, "Verification Success", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(VerifyNumberActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }
    }
    private String generateVerificationCode() {
        int code = (int) (Math.random() * 9000) + 1000; // Generate a random 4-digit code
        return String.valueOf(code);
    }
    public void send(){
        String num = "+97059413842";
         code = generateVerificationCode();
         try {
             Log.i("Tag1","e.getMessage()");
             SmsManager smsManager = SmsManager.getDefault();
             smsManager.sendTextMessage(num,null,code,null,null);
         }catch (Exception e){
             Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
             Log.i("Tag1",e.getMessage());
         }
    }
    public  void sendSMS(Context context, String incomingNumber, String sms) {
        SmsManager smsManager = SmsManager.getDefault();                                      //send sms
        try {
            ArrayList<String> parts = smsManager.divideMessage(sms);
            smsManager.sendMultipartTextMessage(incomingNumber, null, parts, null, null);
            Log.v("ranjith", "Sms to be sent is " + sms);
        } catch (Exception e) {
            Log.v("ranjith", e + "");
        }
    }


}