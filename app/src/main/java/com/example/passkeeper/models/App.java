package com.example.passkeeper.models;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class App {
    private final String name, password, email, imageUrl;

    public static App fromMap(HashMap<String, Object> appMap) {
        String name = (String) appMap.get("name");
        String password = (String) appMap.get("password");
        String email = (String) appMap.get("email");
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

    public enum Tag {SOCIAL_MEDIA, WEBSITE, EMAIL, BANK}

    private final Tag tag;

    public App(String name, String password, String email, Tag tag, String imageUrl) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.tag = tag;
        this.imageUrl = imageUrl;
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

    public String getEmail() {
        return email;
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
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", tag=" + tag +
                '}';
    }
}
