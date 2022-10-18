package com.example.prm_final_project.Model;

import java.util.ArrayList;

public class User {
    private String userId;
    private String password;
    private String username;
    private String avatar;
    private String phone;
    private String email;
    private ArrayList<String> myDeck;
    private boolean isActivate;

    public User() {
    }

    public User(String userId, String password, String username, String avatar, String phone, String email, ArrayList<String> myDeck, boolean isActivate) {
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.myDeck = myDeck;
        this.isActivate = isActivate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }

    public ArrayList<String> getMyDeck() {
        return myDeck;
    }

    public void setMyDeck(ArrayList<String> myDeck) {
        this.myDeck = myDeck;
    }
}
