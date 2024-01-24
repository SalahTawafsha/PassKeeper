package com.example.passkeeper.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passkeeper.R;
import com.example.passkeeper.models.App;
import com.example.passkeeper.models.PasswordGenerator;
import com.example.passkeeper.models.PasswordStrengthChecker;
import com.example.passkeeper.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddPasswordActivity extends AppCompatActivity {
    private EditText appNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView passwordStrengthTextView;
    private ImageButton openedEyeButton;
    private ImageButton closedEyeButton;
    private Spinner appTag;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        emailEditText = findViewById(R.id.emailEditText);
        appNameEditText = findViewById(R.id.appNameTextView);
        openedEyeButton = findViewById(R.id.openedEyeButton);
        closedEyeButton = findViewById(R.id.closedEyeButton);

        passwordEditText = findViewById(R.id.passwordEditText);
        passwordStrengthTextView = findViewById(R.id.passwordStrengthTextView);
        passwordEditText.addTextChangedListener(new MyTextWatcher());

        appTag = findViewById(R.id.appTag);
        fillSpinner();
    }

    private void fillSpinner() {
        // Create an ArrayAdapter using a string array and a default spinner layout
        CharSequence[] options = getResources().getTextArray(R.array.app_tags_options);
        WhiteArrayAdapter adapter = new WhiteArrayAdapter(this, android.R.layout.simple_spinner_item, options);


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        appTag.setAdapter(adapter);

    }

    public void addApp(View view) {
        if (validate()) {
            String appName = appNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            App.Tag tag = getTag();
            String link = getLink(appName, tag);

            App newApp = new App(appName, password, email, tag, link);

            SharedPreferences sharedPref = getSharedPreferences(
                    getString(R.string.login)
                    , Context.MODE_PRIVATE);

            fireStore.collection("users").document(sharedPref.getString("logInEmail", "")).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = User.fromMap(documentSnapshot);

                        user.getApps().add(newApp);
                        try {
                            fireStore.collection("users").document(sharedPref.getString("logInEmail", "")).set(user);
                            openHomePage(view);
                        } catch (Exception e) {
                            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

    private String getLink(String appName, App.Tag tag) {
        if (containsIgnoreCase(appName, "gmail"))
            return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/gmail_icon.png?alt=media&token=64cd89b3-6696-4bfc-a39a-4a7bd06e414a";
        else if (containsIgnoreCase(appName, "Bank of Palestine")) {
            return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/image_2024-01-24_032840177.png?alt=media&token=f3ce3f0e-57ec-4163-849a-912d72deed7a";
        } else if (containsIgnoreCase(appName, "facebook")) {
            return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/image_2024-01-24_033023712.png?alt=media&token=8330a53e-26e4-4db3-856b-e59418f4e1cc";
        } else if (containsIgnoreCase(appName, "instagram")) {
            return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/image_2024-01-24_033030506.png?alt=media&token=46099c4a-5579-40b5-9554-8046964c9c7c";
        } else if (containsIgnoreCase(appName, "youtube")) {
            return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/image_2024-01-24_033110831.png?alt=media&token=e6187871-a095-42c2-88ce-bbce68389506";
        } else if (containsIgnoreCase(appName, "linkedin")) {
            return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/image_2024-01-24_033130047.png?alt=media&token=edf0d243-8eb1-41e6-a661-d2dccb7b5e6a";
        } else if (containsIgnoreCase(appName, "amazon")) {
            return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/image_2024-01-24_033223270.png?alt=media&token=2252a348-de97-437d-8741-f5a37b3011bd";
        } else if (containsIgnoreCase(appName, "hackerrank")) {
            return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/image_2024-01-24_033357636.png?alt=media&token=b99ba216-3c5f-4387-a198-480e3e5e6885";
        }


        switch (tag) {
            case EMAIL:
                return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/emai.png?alt=media&token=74c29a0a-2c06-4262-aa23-f2592a28bbfc";
            case BANK:
                return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/bank.png?alt=media&token=8880521b-c77b-486e-946a-a337d1307109";
            case WEBSITE:
                return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/website.png?alt=media&token=1cb1c943-a201-4870-8a58-c9dbd9d5ce1c";
            default:
                return "https://firebasestorage.googleapis.com/v0/b/passkeeper-2c3b7.appspot.com/o/social%2Cpng.png?alt=media&token=abea90a7-c3fd-4991-a9af-100f5a6adc86";
        }

    }

    private boolean containsIgnoreCase(String appName, String str) {
        return appName.toLowerCase().contains(str.toLowerCase());
    }

    private App.Tag getTag() {
        CharSequence selectedAppTag = (CharSequence) appTag.getSelectedItem();
        switch (selectedAppTag.toString()) {
            case "Social Media":
                return App.Tag.SOCIAL_MEDIA;
            case "Email Account":
                return App.Tag.EMAIL;
            case "Bank Account":
                return App.Tag.BANK;
            case "Website":
                return App.Tag.WEBSITE;
        }
        return App.Tag.EMAIL;
    }

    private boolean validate() {
        // validate that username is just has letters, . and _ using regular expression
        if (appNameEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "App name can't be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // validate email regular expression
        if (!emailEditText.getText().toString().matches(getString(R.string.email_regular_expression)) && isPhoneNumber(emailEditText.getText().toString())) {
            Toast.makeText(this, "This is not email or phone number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // validate password length is at least 8 characters
        if (passwordStrengthTextView.getText().toString().equals("Weak")) {
            Toast.makeText(this, "Password must be at least 8 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isPhoneNumber(String email) {
        if (email.length() == 10) {
            for (int i = 0; i < email.length(); i++) {
                if (!Character.isDigit(email.charAt(i)))
                    return false;
            }
        }
        return false;
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

    public void generatePassword(View view) {
        String randomPassword = PasswordGenerator.generatePassword(10);
        passwordEditText.setText(randomPassword);
    }

    public void openHomePage(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        overridePendingTransition(0, 0);
        this.finish();
    }

    public void openProfilePage(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        overridePendingTransition(0, 0);
        this.finish();

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
                    passwordStrengthTextView.setTextColor(ContextCompat.getColor(AddPasswordActivity.this, R.color.red));
                    break;
                case MEDIUM:
                    passwordStrengthTextView.setText(R.string.medium_password_strength);
                    passwordStrengthTextView.setTextColor(ContextCompat.getColor(AddPasswordActivity.this, R.color.orange));
                    break;
                case STRONG:
                    passwordStrengthTextView.setText(R.string.strong_password_strength);
                    passwordStrengthTextView.setTextColor(ContextCompat.getColor(AddPasswordActivity.this, R.color.green));
                    break;

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public class WhiteArrayAdapter extends ArrayAdapter<CharSequence> {

        public WhiteArrayAdapter(Context context, int resource, CharSequence[] objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        private View getCustomView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) super.getView(position, convertView, parent);
            if (position % 2 == 0)
                textView.setBackgroundColor(getResources().getColor(R.color.search_background));
            else
                textView.setBackgroundColor(getResources().getColor(R.color.line_color));
            textView.setTextColor(Color.WHITE); // Change the text color as needed
            textView.setHeight(100);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            return textView;
        }
    }

}