package com.example.passkeeper.models;


import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
    private final String email, userName, phoneNumber;
    private final List<App> apps;
    private final List<Notification> notifications;

    public User(String email, String userName, String phoneNumber) {
        this.email = email;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.apps = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public User(String email, String userName, String phoneNumber, List<App> apps, List<Notification> notifications) {
        this.email = email;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.apps = apps;
        this.notifications = notifications;
    }

    public static User fromMap(DocumentSnapshot documentSnapshot) {
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

        return new User(userEmail, userUserName, userPhoneNumber, apps, notifications);
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<App> getApps() {
        return apps;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

}
