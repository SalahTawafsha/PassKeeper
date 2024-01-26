package com.example.passkeeper.models;


import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
    private final String email, userName, phoneNumber;
    private final List<App> apps;
    private boolean emailVerified, phoneNumberVerified;

    public User(String email, String userName, String phoneNumber) {
        this.email = email;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.apps = new ArrayList<>();
    }

    public User(String email, String userName, String phoneNumber, List<App> apps) {
        this.email = email;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.apps = apps;
    }

    public User(String email, String userName, String phoneNumber, List<App> apps, boolean emailVerified, boolean phoneNumberVerified) {
        this.email = email;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.apps = apps;
        this.emailVerified = emailVerified;
        this.phoneNumberVerified = phoneNumberVerified;
    }

    public static User fromMap(DocumentSnapshot documentSnapshot) {
        List<App> apps = new ArrayList<>();

        // Check if the "apps" field exists and is not null
        Object appsObject = documentSnapshot.get("apps");
        if (appsObject instanceof List<?>) {
            List<?> appsList = (List<?>) appsObject;

            for (Object app : appsList) {
                if (app instanceof HashMap<?, ?>) {
                    // Suppress unchecked cast because we know that the HashMap is of type <String, Object> by firebase
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> appMap = (HashMap<String, Object>) app;
                    apps.add(App.fromMap(appMap));
                }
            }
        }

        String userEmail = documentSnapshot.getString("email");
        String userUserName = documentSnapshot.getString("userName");
        String userPhoneNumber = documentSnapshot.getString("phoneNumber");
        boolean isEmailVerified = documentSnapshot.getBoolean("emailVerified");
        boolean isPhoneNumberVerified = documentSnapshot.getBoolean("phoneNumberVerified");

        return new User(userEmail, userUserName, userPhoneNumber, apps, isEmailVerified, isPhoneNumberVerified);
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

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public void setPhoneNumberVerified(boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }
}
