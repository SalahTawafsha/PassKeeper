package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.passkeeper.R;
import com.example.passkeeper.models.App;
import com.example.passkeeper.models.AppAdapter;
import com.example.passkeeper.models.Notification;
import com.example.passkeeper.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView list;
    private TextView userName;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userName = findViewById(R.id.user_name);
        list = findViewById(R.id.recyclerView);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        loadCardView();


    }

    private void loadCardView() {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.login)
                , Context.MODE_PRIVATE);

        fireStore.collection("users").document(sharedPref.getString("logInEmail", "")).get()
                .addOnSuccessListener(documentSnapshot -> {

                    List<App> apps = new ArrayList<>();
                    for (Object app : (List<Object>) documentSnapshot.get("apps")) {
                        HashMap<String, Object> appMap = (HashMap<String, Object>) app;
                        apps.add(App.fromMap(appMap));
                    }

                    List<Notification> notifications = new ArrayList<>();
                    for (Object notification : (List<Object>) documentSnapshot.get("notifications")) {
                        HashMap<String, Object> notificationMap = (HashMap<String, Object>) notification;

                        notifications.add(new Notification((String) notificationMap.get("notification")));
                    }

                    String userEmail = documentSnapshot.getString("email");
                    String userUserName = documentSnapshot.getString("userName");
                    String userPhoneNumber = documentSnapshot.getString("phoneNumber");

                    User user = new User(userEmail, userUserName, userPhoneNumber, apps, notifications);

                    userName.setText(user.getUserName());
                    AppAdapter adapter = new AppAdapter(user.getApps());
                    list.setAdapter(adapter);
                });
    }

    public void openNotifications(View view) {
    }

    public void openAddPassword(View view) {
    }

    public void openHomePage(View view) {
    }

    public void openProfilePage(View view) {
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
}