package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.passkeeper.R;
import com.example.passkeeper.models.App;
import com.example.passkeeper.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AppInfoPage extends AppCompatActivity {
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private App currentApp;
    private EditText email;
    private EditText password;
    private ImageView ImageUrl;
    private ConstraintLayout layout;
    private TextView appName;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextpassword);
        ImageUrl = findViewById(R.id.icon_name);

        email.addTextChangedListener(new MyTextWatcher());
        password.addTextChangedListener(new MyTextWatcher());

        appName = findViewById(R.id.AppName);
        layout = findViewById(R.id.AppinfoLayout);
        ImageButton editButton = findViewById(R.id.editbuttonAppInfo);
        delete = findViewById(R.id.delete_app_button);
        editButton.setOnClickListener(v -> {
            if (password.getInputType() == 129) {

                password.setClickable(true);
                password.setInputType(1);
            } else {
                password.setClickable(false);
                password.setInputType(129);
            }
        });
        loadAppInfo();
    }

    private void loadAppInfo() {

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.login)
                , Context.MODE_PRIVATE);

        fireStore.collection("users").document(sharedPref.getString("logInEmail", "")).get()
                .addOnSuccessListener(documentSnapshot -> {

                    User user = User.fromMap(documentSnapshot);
                    List<App> apps = user.getApps();


                    int appPosition = getIntent().getIntExtra("AppPosition", 0);
                    currentApp = apps.get(appPosition);
                    email.setText(currentApp.getEmailOrPhoneNumber());
                    Picasso.get().load(currentApp.getImageUrl()).into(ImageUrl);
                    layout.setBackgroundResource(getBackgroundColor(currentApp));
                    appName.setText(currentApp.getName());
                    password.setText(currentApp.getPassword());


                    delete.setOnClickListener(v -> {
                        if (delete.getText().toString().equals(getText(R.string.deletebutton).toString())) {
                            apps.remove(currentApp);
                        } else {

                            App newApp = new App(currentApp.getName(), password.getText().toString(), email.getText().toString(), currentApp.getTag(), currentApp.getImageUrl(), currentApp.getLastPasswordUpdate());
                            apps.set(appPosition, newApp);
                        }
                        FirebaseFirestore.getInstance().collection("users").document(sharedPref.getString("logInEmail", "")).set(user);
                        finish();


                    });


                });
    }

    private int getBackgroundColor(App app) {
        switch (app.getTag()) {
            case EMAIL:
                return R.drawable.gmail_background;
            case BANK:
                return R.drawable.bankaccount_background;
            case WEBSITE:
                return R.drawable.webaccount_background;
            case SOCIAL_MEDIA:
                return R.drawable.socialmedia_background;
        }
        return 0;
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!email.getText().toString().equals(currentApp.getEmailOrPhoneNumber()) || !password.getText().toString().equals(currentApp.getPassword())) {
                delete.setText(getText(R.string.save));
            } else {
                delete.setText(getText(R.string.deletebutton));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}