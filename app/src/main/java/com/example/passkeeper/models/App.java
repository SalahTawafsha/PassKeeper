package com.example.passkeeper.models;


import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.util.HashMap;

public class App {
    private final String name, password, emailOrPhoneNumber, imageUrl;
    private final Timestamp lastPasswordUpdate;

    public enum Tag {SOCIAL_MEDIA, WEBSITE, EMAIL, BANK}

    private final Tag tag;

    public App(String name, String password, String email, Tag tag, String imageUrl, Timestamp lastPasswordUpdate) {
        this.password = password;
        this.name = name;
        this.emailOrPhoneNumber = email;
        this.tag = tag;
        this.imageUrl = imageUrl;
        this.lastPasswordUpdate = lastPasswordUpdate;
    }

    public static App fromMap(HashMap<String, Object> appMap) {
        String name = (String) appMap.get("name");
        String password = (String) appMap.get("password");
        password = StringRotationCipher.decrypt(password, name.length() % 8 != 0 ? name.length() % 8 : 1);
        String email = (String) appMap.get("emailOrPhoneNumber");
        String imageUrl = (String) appMap.get("imageUrl");
        String tag = (String) appMap.get("tag");
        Timestamp lastPasswordUpdate = (Timestamp) appMap.get("lastPasswordUpdate");

        if (tag != null)
            if (tag.equals("SOCIAL_MEDIA")) {
                return new App(name, password, email, Tag.SOCIAL_MEDIA, imageUrl, lastPasswordUpdate);
            } else if (tag.equals("WEBSITE")) {
                return new App(name, password, email, Tag.WEBSITE, imageUrl, lastPasswordUpdate);
            } else if (tag.equals("EMAIL")) {
                return new App(name, password, email, Tag.EMAIL, imageUrl, lastPasswordUpdate);
            } else if (tag.equals("BANK")) {
                return new App(name, password, email, Tag.BANK, imageUrl, lastPasswordUpdate);
            }
        return null;
    }

    public boolean isOldPassword() {
        if (lastPasswordUpdate != null) {
            long lastPasswordUpdateMillis = lastPasswordUpdate.toDate().getTime();
            long currentTimeMillis = System.currentTimeMillis();
            long diff = currentTimeMillis - lastPasswordUpdateMillis;
            return diff > 1000L * 60 * 60 * 24 * 30 * 3; // 3 months
        }

        return true;
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

    public Timestamp getLastPasswordUpdate() {
        return lastPasswordUpdate;
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
