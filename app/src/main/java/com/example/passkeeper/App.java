package com.example.passkeeper;

public class App {
   protected String AppName, AppPassword, AppEmail,Tags;
   protected int AppNumber;

    public App(String appName, String appPassword, int appNumber, String tags) {
        AppName = appName;
        AppPassword = appPassword;
        AppNumber = appNumber;
        Tags = tags;
    }
    public App(String appName, String appPassword, String appEmail, String tags) {
        AppName = appName;
        AppPassword = appPassword;
        AppEmail = appEmail;
        Tags = tags;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getAppPassword() {
        return AppPassword;
    }

    public void setAppPassword(String appPassword) {
        AppPassword = appPassword;
    }

    public String getAppEmail() {
        return AppEmail;
    }

    public void setAppEmail(String appEmail) {
        AppEmail = appEmail;
    }

    public int getAppNumber() {
        return AppNumber;
    }

    public void setAppNumber(int appNumber) {
        AppNumber = appNumber;
    }
}
