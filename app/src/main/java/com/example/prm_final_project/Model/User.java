package com.example.prm_final_project.Model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class User {
    private String userId;
    private String username;
    private String avatar;
    private String phone;
    private String email;
    private ArrayList<RecentDeck> myDeck;
    private ArrayList<RecentDeck> recentDecks;
    private Hashtable<String,Double> rate;

    public User() {
    }
    public User(String userId, String username, String avatar, String phone, String email) {
        this.userId = userId;
        this.username = username;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
    }
    public User(String userId, String username, String avatar, String phone, String email,ArrayList<RecentDeck> myDeck) {
        this.userId = userId;
        this.username = username;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.myDeck = myDeck;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public ArrayList<RecentDeck> getMyDeck() {
        return myDeck;
    }

    public void setMyDeck(ArrayList<RecentDeck> myDeck) {
        this.myDeck = myDeck;
    }

    public Hashtable<String, Double> getRate() {
        return rate;
    }

    public void setRate(Hashtable<String, Double> rate) {
        this.rate = rate;
    }

    public ArrayList<RecentDeck> getRecentDecks() {
        return recentDecks;
    }

    public void setRecentDecks(ArrayList<RecentDeck> recentDecks) {
        this.recentDecks = recentDecks;
    }
}
