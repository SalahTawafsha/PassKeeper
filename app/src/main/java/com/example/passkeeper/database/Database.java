package com.example.passkeeper.database;

import com.example.passkeeper.App;

import java.util.List;

public interface Database {

    void addUser(); // TODO: add user class

    void updateUser(); // TODO: add user class

    void addNotification(String userEmail, String notification);

    void deleteNotification(String userEmail, String notification);

    List<String> getNotifications(String userEmail);

    void addApp(App app, String userEmail);

    void deleteApp(String appName, String userEmail);

    void updateApp(String appName, App updatedApp, String userEmail);

    App getApp(String appName, String userEmail);

    List<App> getApps(String userEmail);


}
