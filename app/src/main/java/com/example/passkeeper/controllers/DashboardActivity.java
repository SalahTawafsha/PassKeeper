package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.passkeeper.R;
import com.example.passkeeper.models.App;
import com.example.passkeeper.models.AppAdapter;
import com.example.passkeeper.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView list;
    private TextView userName;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private List<App> apps;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userName = findViewById(R.id.user_name);
        list = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);

        searchEditText.addTextChangedListener(new MyTextWatcher());

        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadCardView();
    }

    private void loadCardView() {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.login)
                , Context.MODE_PRIVATE);

        fireStore.collection("users").document(sharedPref.getString("logInEmail", "")).get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = User.fromMap(documentSnapshot);
                    userName.setText(user.getUserName());

                    apps = user.getApps();
                    Log.i("apps", apps.toString());

                    ArrayList<App> searchApps = new ArrayList<>(user.getApps().size());
                    for (App app : apps) {
                        if (app.getName().toLowerCase().contains(searchEditText.getText().toString().toLowerCase())) {
                            searchApps.add(app);
                        }
                    }
                    Log.i("searchApps", searchApps.toString());

                    AppAdapter adapter = new AppAdapter(searchApps);
                    list.setAdapter(adapter);
                });
    }

    public void openNotifications(View view) {
        List<App> oldPasswordApps = new ArrayList<>();
        for (App app : apps) {
            if (app.isOldPassword()) {
                oldPasswordApps.add(app);
            }

        }

        // ToDo: Open notifications page and show oldPasswordApps list with message
    }

    public void openAddPassword(View view) {
        Intent intent = new Intent(this, AddPasswordActivity.class);
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

    public void openTutorial(View view) {
        // Inflate the pop-up layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.details_popup, null);

        // Create the PopupWindow
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // if you want the pop-up to dismiss when touched outside
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Set up the close button in the pop-up
        ImageButton closeButton = popupView.findViewById(R.id.exit_button);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        // Show the pop-up at the center of yourView
        popupWindow.showAtLocation(findViewById(R.id.dashboardActivity), Gravity.CENTER, 0, 0);

    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            loadCardView();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}