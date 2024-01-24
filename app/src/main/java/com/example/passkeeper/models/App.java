package com.example.passkeeper.models;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class App {
    private final String name, password, emailOrPhoneNumber, imageUrl;

    public enum Tag {SOCIAL_MEDIA, WEBSITE, EMAIL, BANK}

    private final Tag tag;

    public App(String name, String password, String email, Tag tag, String imageUrl) {
        this.password = password;
        this.name = name;
        this.emailOrPhoneNumber = email;
        this.tag = tag;
        this.imageUrl = imageUrl;
    }

    public static App fromMap(HashMap<String, Object> appMap) {
        String name = (String) appMap.get("name");
        String password = (String) appMap.get("password");
        password = StringRotationCipher.decrypt(password, name.length() % 8 != 0 ? name.length() % 8 : 1);
        String email = (String) appMap.get("emailOrPhoneNumber");
        String imageUrl = (String) appMap.get("imageUrl");
        String tag = (String) appMap.get("tag");

        if (tag != null)
            if (tag.equals("SOCIAL_MEDIA")) {
                return new App(name, password, email, Tag.SOCIAL_MEDIA, imageUrl);
            } else if (tag.equals("WEBSITE")) {
                return new App(name, password, email, Tag.WEBSITE, imageUrl);
            } else if (tag.equals("EMAIL")) {
                return new App(name, password, email, Tag.EMAIL, imageUrl);
            } else if (tag.equals("BANK")) {
                return new App(name, password, email, Tag.BANK, imageUrl);
            }
        return null;
    }


    public Tag getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailOrPhoneNumber() {
        return emailOrPhoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "App{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + emailOrPhoneNumber + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", tag=" + tag +
                '}';
    }
}
