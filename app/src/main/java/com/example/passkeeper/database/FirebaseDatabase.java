package com.example.passkeeper.database;

import com.example.passkeeper.App;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FirebaseDatabase implements Database {
    private static FirebaseDatabase instance;
//    private

    @Override
    public void addUser() {
    }

    @Override
    public void updateUser() {

    }

    @Override
    public void addNotification(String userEmail, String notification) {

    }

    @Override
    public void deleteNotification(String userEmail, String notification) {

    }

    @Override
    public List<String> getNotifications(String userEmail) {
        return null;
    }

    @Override
    public void addApp(App app, String userEmail) {

    }

    @Override
    public void deleteApp(String appName, String userEmail) {

    }

    @Override
    public void updateApp(String appName, App updatedApp, String userEmail) {

    }

    @Override
    public App getApp(String appName, String userEmail) {
        return null;
    }

    @Override
    public List<App> getApps(String userEmail) {
        return null;
    }
}
