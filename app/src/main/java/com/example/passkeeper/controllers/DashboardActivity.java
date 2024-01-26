package com.example.passkeeper.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private ListView notificationList;

    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userName = findViewById(R.id.user_name);
        list = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);

        searchEditText.addTextChangedListener(new MyTextWatcher());

        ImageButton notification = findViewById(R.id.notificationButton);
        notificationList = findViewById(R.id.notificationListView);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        notification.setOnClickListener(v -> openNavigationDrawer());

    }

    @Override
    public void onBackPressed() {
        // Check if the current activity is the DashboardActivity
        if (getClass().getSimpleName().equals("DashboardActivity")) {
            // You can show a Toast message or take any other action
            Toast.makeText(this, "Back button press disabled on Dashboard", Toast.LENGTH_SHORT).show();
        } else {
            // Call the default behavior to allow navigating back for other activities
            super.onBackPressed();
        }
    }

    private void openNavigationDrawer() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            drawerLayout.openDrawer(Gravity.RIGHT);
            openNotifications();
        }
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
                    apps = user.getApps();

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

    public void openNotifications() {
        List<String> notificationMessages = new ArrayList<>();

        for (App app : apps) {
            if (app.isOldPassword()) {
                notificationMessages.add("Password of " + app.getName() + " is too old  , please update it");
            }

            ArrayAdapter<String> notificationAdapter = (ArrayAdapter<String>) notificationList.getAdapter();
            if (notificationAdapter != null) {
                notificationAdapter.clear();
                notificationAdapter.addAll(notificationMessages);
            } else {
                notificationAdapter = new ArrayAdapter<>(
                        this,
                        R.layout.listview, // Your custom layout for each item
                        R.id.notificationTextView, // The ID of the TextView in the layout
                        notificationMessages // Your data source
                );

                // Set the adapter to your ListView
                notificationList.setAdapter(notificationAdapter);
                // notificationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notificationMessages);
                notificationList.setAdapter(notificationAdapter);
            }

            notificationAdapter.notifyDataSetChanged();

        }
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