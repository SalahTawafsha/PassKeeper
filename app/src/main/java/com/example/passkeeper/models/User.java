package com.example.passkeeper.models;


import java.util.ArrayList;
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
